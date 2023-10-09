from pandas import DataFrame
from pandas import Series
from pandas import concat
from pandas import read_csv
from pandas import datetime
from sklearn.metrics import mean_squared_error
from sklearn.preprocessing import MinMaxScaler
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
from math import sqrt
from matplotlib import pyplot
from numpy import array
import numpy as np
import matplotlib.pyplot as plt
from datetime import datetime
import csv
import sys
# from pylab import *
# mpl.rcParams['font.sans-serif'] = ['SimHei'] # 指定默认字体  还有仿宋、楷体、微软雅黑：'FangSong', 'KaiTi', 'Microsoft YaHei'
# mpl.rcParams['axes.unicode_minus'] = False # 解决保存图像是负号'-'显示为方块的问题 


# 加载数据集
def parser(x):
    return datetime.strptime(x, '%Y/%m/%d')



# 将时间序列转换为监督类型的数据序列
def series_to_supervised(data, n_in=1, n_out=1, dropnan=True):
    n_vars = 1 if type(data) is list else data.shape[1]
    df = DataFrame(data)
    cols, names = list(), list()
    # 这个for循环是用来输入列标题的 var1(t-1)，var1(t)，var1(t+1)，var1(t+2)
    for i in range(n_in, 0, -1):
        cols.append(df.shift(i))
        names += [('var%d(t-%d)' % (j + 1, i)) for j in range(n_vars)]
    # 转换为监督型数据的预测序列 每四个一组，对应 var1(t-1)，var1(t)，var1(t+1)，var1(t+2)
    for i in range(0, n_out):
        cols.append(df.shift(-i))
        if i == 0:
            names += [('var%d(t)' % (j + 1)) for j in range(n_vars)]
        else:
            names += [('var%d(t+%d)' % (j + 1, i)) for j in range(n_vars)]
    # 拼接数据
    agg = concat(cols, axis=1)
    agg.columns = names
    # 把null值转换为0
    if dropnan:
        agg.dropna(inplace=True)
    print(agg)
    return agg


# 对传入的数列做差分操作，相邻两值相减
def difference(dataset, interval=1):
    diff = list()
    for i in range(interval, len(dataset)):
        value = dataset[i] - dataset[i - interval]
        diff.append(value)
    return Series(diff)

# MinMaxScaler的方法：axis=0表示要在列上取值，注意这里需要输入的值是二维的
   # X_std = (X - X.min(axis=0)) / (X.max(axis=0) - X.min(axis=0))
    #     X_scaled = X_std * (max - min) + min
    #
    # where min, max = feature_range.

# 将序列转换为用于监督学习的训练和测试集，差分，缩放，转化有监督
def prepare_data(series, n_test, n_lag, n_seq):
    # 提取原始值即株高数据[2.7 4.3 5.2 5.9 6.9 7.3 8.3]
    # 将数据转换为静态的
    diff_series = difference(series, 1)
    # 做差[1.6 0.9 0.7 1.  0.4 1. ]
    diff_values = diff_series.values
    diff_values = diff_values.reshape(len(diff_values), 1)#注意缩放需要输入的是二维数据
    print("diff_values")
    print(diff_values)
    print()
    # 重新调整数据为（-1,1）之间
    scaler = MinMaxScaler(feature_range=(-1, 1))
    scaled_values = scaler.fit_transform(diff_values)
    scaled_values = scaled_values.reshape(len(scaled_values), 1)
    # 转化为有监督的数据X，y
    supervised = series_to_supervised(scaled_values, n_lag, n_seq)
    supervised_values = supervised.values
    print("supervised_values")
    print(supervised_values)
    # 分割为测试数据和训练数据
    train, test = supervised_values[0:-n_test], supervised_values[-n_test:]
    return scaler, train, test


# 匹配LSTM网络训练数据
def fit_lstm(train, n_lag, n_seq, n_batch, nb_epoch, n_neurons):
    # 重塑训练数据格式 [samples, timesteps, features]
    X, y = train[:, 0:n_lag], train[:, n_lag:]
    X = X.reshape(X.shape[0], 1, X.shape[1])
    # 配置一个LSTM神经网络，添加网络参数
    model = Sequential()
    model.add(LSTM(n_neurons, batch_input_shape=(n_batch, X.shape[1], X.shape[2]), stateful=True))#意味着由每个batch计算出的状态都会被重用于初始化下一个batch的初始状态。状态RNN假设连续的两个batch之中，相同下标的元素有一一映射关系。
    model.add(Dense(y.shape[1]))#因为这里是多步预测，如果要预测三步，那么最后输出肯定是三个值，也就是说最后一层有三个神经元输出三个值
    print(model.summary())
    model.compile(loss='mean_squared_error', optimizer='adam')
    # 调用网络，迭代数据对神经网络进行训练，最后输出训练好的网络模型
    for i in range(nb_epoch):
        history = model.fit(X, y, epochs=1, batch_size=n_batch, verbose=0, shuffle=False)
        model.reset_states()#要手动进行epoch循环，这样才会在每个epoch结束的时候再reset_states,删除内部状态。如果直接使用epochs=nb_epoch,不手动循环，则网络会在每个batch结束的时候就reset_states,
        #充值状态只是消除内部状态，但并不是重置参数，所以每个epoch之间也不是相互独立的。
    # plt.plot(history.history['val_loss'])
    return model
    # model.fit(X, y, epochs=n_epochs, batch_size=n_batch, verbose=0, shuffle=False)


# 用LSTM做预测
def forecast_lstm(model, X, n_batch):
    print("reshape前X:")
    print(X)
    # 重构输入参数 [samples, timesteps, features]
    X = X.reshape(1, 1, len(X))#每次只测试一个，所以samples直接为1，特征为一步的特征长度，如果使用两步进行预测，就是使用前两个数据进行预测，因为是单变量，所以特征大小就是2
    print("reshape后X:")
    print(X)
    # 开始预测
    forecast = model.predict(X, batch_size=n_batch)
    # 结果转换成数组
    return [x for x in forecast[0, :]]


# 利用训练好的网络模型，对测试数据进行预测
def make_forecasts(model, n_batch, train, test, n_lag, n_seq):
    forecasts = list()
    # 预测方式是用一个X值预测出后三步的Y值
    for i in range(len(test)):
        X, y = test[i, 0:n_lag], test[i, n_lag:]
        print
        # 调用训练好的模型预测未来数据
        forecast = forecast_lstm(model, X, n_batch)
        # 将预测的数据保存
        forecasts.append(forecast)
    print("这里是最初的预测结构")
    print(np.asarray(forecast).shape)
    return forecasts


# 对预测后的缩放值（-1，1）进行逆变换
def inverse_difference(last_ob, forecast):
    # invert first forecast
    inverted = list()
    inverted.append(forecast[0] + last_ob)#先将起始值与差分的第一个值1进行相加，得到了原数据的2。，之后就是无限的将前一个数据与差分相加得到下一个数据的过程
    # propagate difference forecast using inverted first value
    for i in range(1, len(forecast)):
        print(forecast[i] + inverted[i - 1])
        inverted.append(forecast[i] + inverted[i - 1])
    return inverted



# 对预测完成的数据进行逆变换
def inverse_transform(series, forecasts, scaler, n_test):
    inverted = list()
    for i in range(len(forecasts)):
        # create array from forecast
        forecast = array(forecasts[i])
        forecast = forecast.reshape(1, len(forecast))#(1,3)
        print("预测")
        print(forecast)
        # 将预测后的数据缩放逆转换
        inv_scale = scaler.inverse_transform(forecast)
        inv_scale = inv_scale[0, :]
        print("inv_scale")
        print(inv_scale)
        # invert differencing
        index = len(series) - n_test + i - 1#这n里取的是在开始取取差分的坐标，例如数组[1,2,5,7,,5]，那么差分就是[1,3,2,-2]对应的是原来数据的去掉第一个，如果想要逆差分，就要获取1的索引，
        print(index)
        print(len(series))
        print(i)
        last_ob = series[index]#根据索引获取值为1.
        # 将预测后的数据差值逆转换
        inv_diff = inverse_difference(last_ob, inv_scale)#将起始值为1，和之后的差分列表传入方法，看上面方法继续
        # 保存数据
        print(inv_diff)
        inverted.append(inv_diff)
    return inverted


# 评估每个预测时间步的RMSE
def evaluate_forecasts(series,test, forecasts, n_lag, n_seq,predicteds):
    
    print("这里是现在的测试结构")
    print(np.asarray(test).shape)
    print(test)
    print("这里是现在的预测结构")
    print(np.asarray(forecasts).shape)
    print(forecasts)


    # for i in range(10):
    #     for j in range(n_seq):
    #         actual = test[i]
    #         predicted = forecasts[i]
    #         rmse = sqrt(mean_squared_error(actual, predicted))
    #         print('t+%d RMSE: %f' % ((j + 1), rmse))
    actual = []
    actual_1 = series[6]
    actual.append(actual_1)
    for i in range(n_seq):
        # actual = [row[i] for row in test]
        print('actual:')
        print(actual)
        predicted = [forecast[i] for forecast in forecasts]
        print('predicted:')
        print(predicted)
        predicteds.append(predicted)
        rmse = sqrt(mean_squared_error(actual, predicted))
        print('t+%d RMSE: %f' % ((i + 1), rmse))
        print("误差率")
        # error = (predicted - actual)/actual
        relative_error = np.average(np.abs(np.array(actual) - np.array(predicted)) / np.array(actual), axis=0)
        print(relative_error)

# 在原始数据集的上下文中绘制预测图
def plot_forecasts(series, forecasts, n_test):
    # plot the entire dataset in blue
    pyplot.plot(series,color='black')
    for m in range(len(series)):
        plt.plot(m, series[m], marker='v', color='black')
    # plot the forecasts in red
    for i in range(len(forecasts)):
        off_s = len(series) - n_test + i - 1
        off_e = off_s + len(forecasts[i]) + 1
        xaxis = [x for x in range(off_s, off_e)]
        yaxis = [series[off_s]] + forecasts[i]#将前一天的观察数据与预测数据进行连接，形成长度为4的向量
        pyplot.plot(xaxis, yaxis, color='red')
    plt.plot(off_s+1, forecasts, marker='v', color='r')
    # pyplot.title('LSTM西瓜幼苗苗高预测')
    # pyplot.xlabel('生长日期',
    #         fontdict = {'fontsize' : 15,
    #                     'color':'black'})

    # pyplot.ylabel('西瓜幼苗苗高',
    #             fontdict = {'fontsize' : 15,
    #                         'color':'black'})
    # show the plot
    # pyplot.show()




# series = read_csv('D:\\CNN-LSTM-Project\\data\\1-1.csv', header=0, parse_dates=[0], index_col=0, squeeze=True, date_parser=parser)
# print("加载数据...")
# print(series)

"""
加载数据
"""

def main(file_folder,file_name,save_folder):
    predicteds = []
    with open(file_folder+"\\"+file_name+".csv", 'r') as f:
        reader = csv.reader(f)
        result = list(reader)
        data = []
        for i in range(1,len(result)):
            print(result[i])
            data_1 = []
            for j in range(1,len(result[0])):
                data_1.append(float(result[i][j]))
            print(data_1)
            data.append(data_1)
        print(data)

    # 配置网络信息
    n_lag = 3   # n_lag个数据预测1个数据 训练数据n_lag个数据预测一个数据作为一个训练组
    n_seq = 1   #预测的步长
    n_test = 1
    n_epochs = 100
    n_batch = 1
    n_neurons = 4
    # 准备数据

    for i in range(len(data)):
        data_single = data[i]
        scaler, train, test = prepare_data(data_single, n_test, n_lag, n_seq)
        print("train")
        print(train)
        print("test")
        print(test)
        # 准备预测模型
        model = fit_lstm(train, n_lag, n_seq, n_batch, n_epochs, n_neurons)
        print("训练完成")
        # 开始预测
        print("开始预测")
        forecasts = make_forecasts(model, n_batch, train, test, n_lag, n_seq)
        # 逆转换训练数据和预测数据
        forecasts = inverse_transform(data_single, forecasts, scaler, n_test)
        print(forecasts)
        # 逆转换测试数据
        actual = [row[n_lag:] for row in test]
        print("actual")
        print(actual)
        print("真实测试值")
        actual = inverse_transform(data_single, actual, scaler, n_test)
        print("actual")
        print(actual)
        
        # 比较预测数据和测试数据，计算两者之间的损失值
        evaluate_forecasts(data_single,actual, forecasts, n_lag, n_seq,predicteds)
        # 画图
        # plot_forecasts(data_single, forecasts, n_test+2)
    print(predicteds)
    """
    保存预测结果
    """
    with open(save_folder,"w",newline='') as csvfile:
        writer = csv.writer(csvfile)
        #先写入columns_name
        writer.writerow(["幼苗序号","2022/5/5-9.00"])
        #写入多行用writerows
        for i in range(len(predicteds)):
            writer.writerows([["第"+str(i+1)+"株",predicteds[i][0]]])  


# file_folder = "C:\\Users\\hzaurobot_1\\Desktop\\database\\Experimental csv data\\greenhouse_1\\seedbed_1\\SeedlingTray_1"                  #读取文件路径
# file_name = "height_m"                                                                                                                     #文件名
# save_folder = "C:\\Users\\hzaurobot_1\\Desktop\\database\\Experimental csv data\\greenhouse_1\\seedbed_1\\SeedlingTray_1\\predicted.csv"   #文件保存路径


# main(file_folder,file_name,save_folder)

if __name__ == '__main__':
    a = []
    for i in range(1, len(sys.argv)):
        a.append((sys.argv[i]))
    main(a[0],a[1],a[2])