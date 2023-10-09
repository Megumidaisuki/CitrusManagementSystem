import sys
import cv2
import numpy as np
from matplotlib import pyplot as plt
import xlwt
from scipy.signal import savgol_filter
from scipy.interpolate import make_interp_spline

'''
file_folder为图片读取和保存的路径
csv_file_folder为csv文件读取和保存的路径
file_name为图片日期前缀
'''


# file_folder = "C:\\Users\\hzaurobot_1\\Desktop\\database\\Experimental data\\greenhouse_1\\seedbed_1\\SeedlingTray_1"
# csv_file_folder = "C:\\Users\\hzaurobot_1\\Desktop\\database\\Experimental csv data\\greenhouse_1\\seedbed_1\\SeedlingTray_1"
# file_name = "5.5rgb"
# Path = "C:\\Users\\hzaurobot_1\\Desktop\\database\\Experimental data\\greenhouse_1\\seedbed_1\\SeedlingTray_1\\5.3rgb.png"
# Save_Path = "C:\\Users\\hzaurobot_1\\Desktop\\database\\Experimental data\\greenhouse_1\\seedbed_1\\SeedlingTray_1\\5.3rgb_jiance.png"

def detectLeaf(file_folder, file_name, csv_file_folder):
    img = cv2.imread(file_folder + "\\" + file_name + ".png")
    yuantu = cv2.imread(file_folder + "\\" + file_name + ".png")
    blade_detect = cv2.imread(file_folder + "\\" + file_name + ".png")

    fsrc = np.array(img, dtype=np.float32) / 255.0
    (b, g, r) = cv2.split(fsrc)
    gray = 2 * g - b - r
    # 求取最大值和最小值
    (minVal, maxVal, minLoc, maxLoc) = cv2.minMaxLoc(gray)
    # 计算直方图
    hist = cv2.calcHist([gray], [0], None, [256], [minVal, maxVal])
    gray_u8 = np.array((gray - minVal) / (maxVal - minVal) * 255, dtype=np.uint8)
    # 超绿
    (thresh_lv, chaolv) = cv2.threshold(gray_u8, -1.0, 255, cv2.THRESH_OTSU)
    # 得到彩色的超绿图像
    (b8, g8, r8) = cv2.split(img)
    chaolv_rgb = cv2.merge([b8 & chaolv, g8 & chaolv, r8 & chaolv])
    # 扣去绿色信息只剩穴盘
    num_labels, labels, stats, centroids = cv2.connectedComponentsWithStats(chaolv, connectivity=8)
    # 查看各个返回值
    """
    num_labels : 返回值是连通区域的数量。
    labels : labels是一个与image一样大小的矩形（labels.shape = image.shape），其中每一个连通区域会有一个唯一标识，标识从0开始。
    stats ：stats会包含5个参数分别为x,y,h,w,s。分别对应每一个连通区域的外接矩形的起始坐标x,y；外接矩形的wide,height；s其实不是外接矩形的面积，实践证明是labels对应的连通区域的像素个数。
    centroids : 返回的是连通区域的质心。
    """
    for i in range(1, num_labels):
        pexils = stats[i][4]
        if pexils <= 100:
            continue
        mask = labels == i
        img[:, :, 0][mask] = 0
        img[:, :, 1][mask] = 0
        img[:, :, 2][mask] = 0
    # 手动设置阈值
    # src_gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    # the =100
    # maxval = 255
    # the, thresh = cv2.threshold(src_gray, the, maxval, cv2.THRESH_BINARY)
    # cv2.imshow("th2", thresh)
    # cv2.imwrite("C:\\Users\\hzaurobot_1\\Desktop\\2.26dapeng\\"+file_folder+"\\thresh.jpg", thresh)
    # 自适应阈值
    src_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    the, thresh = cv2.threshold(src_gray, 0, 255, cv2.THRESH_OTSU)
    print("自适应阈值：" + str(the))

    num_labels1, labels1, stats1, centroids1 = cv2.connectedComponentsWithStats(thresh, connectivity=8)
    output_1 = np.zeros((img.shape[0], img.shape[1], 3), np.uint8)
    for i in range(1, num_labels1):
        pexils = stats1[i][4]
        if pexils <= 200:
            continue
        mask = labels1 == i
        output_1[:, :, 0][mask] = 255
        output_1[:, :, 1][mask] = 255
        output_1[:, :, 2][mask] = 255

    kernel = np.ones((5, 5), np.uint8)
    opening = cv2.morphologyEx(thresh, cv2.MORPH_OPEN, kernel)
    num_labels2, labels2, stats2, centroids2 = cv2.connectedComponentsWithStats(opening, connectivity=8)
    output_2 = np.zeros((img.shape[0], img.shape[1], 3), np.uint8)
    for i in range(1, num_labels2):
        pexils = stats2[i][4]
        if pexils <= 300:
            continue
        mask = labels2 == i
        output_2[:, :, 0][mask] = 255
        output_2[:, :, 1][mask] = 255
        output_2[:, :, 2][mask] = 255
    # 统计每行每列像素值
    x = np.zeros((img.shape[1],), dtype=int)
    print(len(x))

    for i in range(img.shape[1]):
        num = 0
        for j in range(img.shape[0]):
            if output_2[j, i, 0] == 255:
                num = num + 1
        # print(num)
        x[i] = num
    print(x)

    y = np.zeros((img.shape[0],), dtype=int)
    print(len(y))
    for i in range(img.shape[0]):
        num = 0
        for j in range(img.shape[1]):
            if output_2[i, j, 0] == 255:
                num = num + 1
        # print(num)
        y[i] = num
    print(y)

    # 写入Excel文档
    workbook = xlwt.Workbook(encoding='utf-8')  # 设置一个workbook，其编码是utf-8
    worksheet = workbook.add_sheet("test_sheet")
    for i in range(len(x)):  # 循环将a和b列表的数据插入至excel
        # worksheet.write(i+1,0,label=x[i])
        worksheet.write(i + 1, 0, int(x[i]))
    for i in range(len(y)):  # 循环将a和b列表的数据插入至excel
        worksheet.write(i + 1, 1, int(y[i]))

    # workbook.save("C:\\Users\\hzaurobot_1\\Desktop\\"+file_folder+"\\xlwt_test.xls")

    # 最大值函数
    def Max(list):
        Max = list[0]
        for i in list:
            if i > Max:
                Max = i
        return Max

    #  x方向统计
    fanwei = 200

    def nms(data):
        length = len(data)
        ans = []
        for i in range(0, length - 2 * fanwei):
            fanwei_list = []
            for j in range(1, fanwei):
                fanwei_number_pre = data[i + fanwei - j]
                fanwei_number_after = data[i + fanwei + j]
                fanwei_list.append(fanwei_number_pre)
                fanwei_list.append(fanwei_number_after)
            if data[i + fanwei] > Max(fanwei_list):
                print("记录")
                ans.append(i + fanwei)
        return ans

    #  y方向统计
    fanwei_y = 100

    def nms_1(data):
        length = len(data)
        ans = []
        for i in range(0, length - 2 * fanwei_y):
            fanwei_list = []
            for j in range(1, fanwei_y):
                fanwei_number_pre = data[i + fanwei_y - j]
                fanwei_number_after = data[i + fanwei_y + j]
                fanwei_list.append(fanwei_number_pre)
                fanwei_list.append(fanwei_number_after)
            if data[i + fanwei_y] > Max(fanwei_list):
                print("记录")
                ans.append(i + fanwei_y)
        return ans

    plt.figure(figsize=(6, 4))
    # Savitzky-Golay 滤波器 曲线平滑
    pinghua = savgol_filter(y, 15, 3, mode='nearest')
    plt.plot(np.arange(len(y)), pinghua)
    point = nms(pinghua)
    plt.plot(point, pinghua[point], "o")
    print("X方向峰值点：" + str(point))
    # plt.show()
    pinghua_y = savgol_filter(x, 15, 3, mode='nearest')
    plt.plot(np.arange(len(x)), pinghua_y)
    point_y = nms_1(pinghua_y)
    plt.plot(point_y, pinghua_y[point_y], "o")
    print("Y方向峰值点：" + str(point_y))
    # plt.show()

    # 检测出穴盘边界后对其进行可视化
    y = point
    x = point_y
    # 标记的参数设置
    point_color = (0, 0, 255)  # BGR
    thickness = 4
    lineType = 4
    thickness_1 = 8
    # chaolv = chaolv_rgb
    chaolv = np.zeros((chaolv_rgb.shape[0], chaolv_rgb.shape[1], 3), np.uint8)
    chaolv[:, :, 0] = chaolv_rgb[:, :, 0]
    chaolv[:, :, 1] = chaolv_rgb[:, :, 1]
    chaolv[:, :, 2] = chaolv_rgb[:, :, 2]
    cv2.imwrite(file_folder + "\\" + file_name + "_detcetchaolv.png", chaolv)

    All_Plug_Seeding = 0
    No_Emergence_Plug_Seeding = 0
    No_Emergence_Plug_Seeding_Location = []
    for j in range(len(y) - 1):
        for i in range(len(x) - 1):
            All_Plug_Seeding = All_Plug_Seeding + 1  # 统计总穴孔数
            print("读取" + str(j + 1) + "-" + str(i + 1))
            # 苗盘横向摆放
            transverse1_ptStart = (x[i], y[j])
            transverse1_ptEnd = (x[i + 1], y[j])
            transverse2_ptStart = (x[i], y[j + 1])
            transverse2_ptEnd = (x[i + 1], y[j + 1])
            machine1_ptStart = (x[i], y[j])
            machine1_ptEnd = (x[i], y[j + 1])
            machine2_ptStart = (x[i + 1], y[j])
            machine2_ptEnd = (x[i + 1], y[j + 1])
            # 苗盘竖向摆放
            # transverse1_ptStart = (y[j], x[i])
            # transverse1_ptEnd = (y[j+1], x[i])
            # transverse2_ptStart = (y[j], x[i+1])
            # transverse2_ptEnd = (y[j+1], x[i+1])
            # machine1_ptStart = (y[j], x[i])
            # machine1_ptEnd = (y[j], x[i+1])
            # machine2_ptStart = (y[j+1], x[i])
            # machine2_ptEnd = (y[j+1], x[i+1])
            # 标记第一张图
            cv2.line(yuantu, transverse1_ptStart, transverse1_ptEnd, point_color, thickness, lineType)
            cv2.line(yuantu, transverse2_ptStart, transverse2_ptEnd, point_color, thickness, lineType)
            cv2.line(yuantu, machine1_ptStart, machine1_ptEnd, point_color, thickness, lineType)
            cv2.line(yuantu, machine2_ptStart, machine2_ptEnd, point_color, thickness, lineType)
            # 标记第二张图
            cv2.line(chaolv_rgb, transverse1_ptStart, transverse1_ptEnd, point_color, thickness, lineType)
            cv2.line(chaolv_rgb, transverse2_ptStart, transverse2_ptEnd, point_color, thickness, lineType)
            cv2.line(chaolv_rgb, machine1_ptStart, machine1_ptEnd, point_color, thickness, lineType)
            cv2.line(chaolv_rgb, machine2_ptStart, machine2_ptEnd, point_color, thickness, lineType)

            # num_labels, labels, stats, centroids = cv2.connectedComponentsWithStats(img[y[j]:y[j+1],x[i]:x[i+1]], connectivity=8)
            # # 查看各个返回值
            # """
            # num_labels : 返回值是连通区域的数量。
            # labels : labels是一个与image一样大小的矩形（labels.shape = image.shape），其中每一个连通区域会有一个唯一标识，标识从0开始。
            # stats ：stats会包含5个参数分别为x,y,h,w,s。分别对应每一个连通区域的外接矩形的起始坐标x,y；外接矩形的wide,height；s其实不是外接矩形的面积，实践证明是labels对应的连通区域的像素个数。
            # centroids : 返回的是连通区域的质心。
            # """
            # for i in range(1, num_labels):
            #     pexils = stats[i][4]
            #     if pexils <= 100:
            #         continue
            #     mask = labels == i
            #     img[y[j]:y[j+1],x[i]:x[i+1]][:, :, 0][mask] = 0
            #     img[y[j]:y[j+1],x[i]:x[i+1]][:, :, 1][mask] = 0
            #     img[y[j]:y[j+1],x[i]:x[i+1]][:, :, 2][mask] = 0
            print("已标记" + str(j + 1) + "-" + str(i + 1))
            # cv2.imwrite("C:\\Users\\hzaurobot_1\\Desktop\\datasets\\"+file_folder+"\\chaolv" + str(i+1) +"-"+str(j+1)+ ".jpg",img[x[i]:x[i+1],y[j]:y[j+1]])
            # cv2.imwrite("C:\\Users\\hzaurobot_1\\Desktop\\datasets\\" + file_folder + "\\rgb" + str(i + 1) + "-" + str(
            #     j + 1) + ".jpg", yuantu[ x[i]:x[i + 1],y[j]:y[j + 1]])
            # pixel = 0
            # all = 0
            # for m in range(x[i],x[i+1]+1):
            #     for n in range(y[j],y[j+1]+1):
            #         all = all + 1
            #         if img[n,m,0] != 0 or img[n,m,1] != 0 or img[n,m,2] != 0:
            #             pixel = pixel + 1
            # # print("有色像素占比" + str((pixel / ((x[i + 1] - x[i]) * (y[i + 1] - y[j]))) * 100) + "%")
            # print(pixel)
            # print(all)
            # print("有色像素占比" + str((pixel / all) * 100) + "%")

            # 补苗决策
            # 单株苗
            # img = img[y:y+w,x:x+h]
            danzhu = chaolv[y[j]:y[j + 1], x[i]:x[i + 1]]
            cv2.imwrite(file_folder + "\\danzhu\\chaolv" + str(j + 1) + "-" + str(i + 1) + ".jpg", danzhu)
            # 二值化 取最小连通域 并且去噪
            src_gray = cv2.cvtColor(danzhu, cv2.COLOR_BGR2GRAY)
            the, thresh = cv2.threshold(src_gray, 0, 255, cv2.THRESH_OTSU)
            num_labels, labels, stats, centroids = cv2.connectedComponentsWithStats(thresh, connectivity=8)
            danzhu_img = np.zeros((danzhu.shape[0], danzhu.shape[1], 3), np.uint8)
            for p in range(1, num_labels):
                pexils = stats[p][4]
                if pexils <= 50:
                    continue
                mask = labels == p
                danzhu_img[:, :, 0][mask] = danzhu[:, :, 0][mask]
                danzhu_img[:, :, 1][mask] = danzhu[:, :, 1][mask]
                danzhu_img[:, :, 2][mask] = danzhu[:, :, 2][mask]
            cv2.imwrite(file_folder + "\\quzao\\chaolv" + str(j + 1) + "-" + str(i + 1) + ".jpg", danzhu_img)
            # 统计有色像素占比
            pixel = 0
            all = danzhu_img.shape[0] * danzhu_img.shape[1]
            for m in range(danzhu_img.shape[1]):
                for n in range(danzhu_img.shape[0]):
                    if danzhu_img[n, m, 0] != 0 or danzhu_img[n, m, 1] != 0 or danzhu_img[n, m, 2] != 0:
                        pixel = pixel + 1
            print(all)
            print("有色像素占比" + str((pixel / all) * 100) + "%")
            print("像素投影统计值：" + str(pixel))
            # 标记像素为零的穴孔即无苗穴孔
            No_location = (0, 0)
            if (pixel / all) * 100 < 5:
                No_Emergence_Plug_Seeding = No_Emergence_Plug_Seeding + 1
                No_location = (j + 1, i + 1)
                No_Emergence_Plug_Seeding_Location.append(No_location)
                # transverse1_ptStart = (x[i], y[j])
                # transverse1_ptEnd = (x[i + 1], y[j])
                # transverse2_ptStart = (x[i], y[j + 1])
                # transverse2_ptEnd = (x[i + 1], y[j + 1])
                # machine1_ptStart = (x[i], y[j])
                # machine1_ptEnd = (x[i], y[j + 1])
                # machine2_ptStart = (x[i + 1], y[j])
                # machine2_ptEnd = (x[i + 1], y[j + 1])
                cv2.line(blade_detect, transverse1_ptStart, transverse1_ptEnd, point_color, thickness_1, lineType)
                cv2.line(blade_detect, transverse2_ptStart, transverse2_ptEnd, point_color, thickness_1, lineType)
                cv2.line(blade_detect, machine1_ptStart, machine1_ptEnd, point_color, thickness_1, lineType)
                cv2.line(blade_detect, machine2_ptStart, machine2_ptEnd, point_color, thickness_1, lineType)

            # 补苗决策
            # All_Plug_Seeding = 0
            # No_Emergence_Plug_Seeding = 0
            # No_Emergence_Plug_Seeding_Location = []
            # for i in range(len(x)-1):
            #     for j in range(len(y)-1):
            #         All_Plug_Seeding = All_Plug_Seeding + 1
            #         print("读取" + str(j + 1) + "-" + str(i + 1))
            #         danzhu = cv2.imread("C:\\Users\\hzaurobot_1\\Desktop\\2.26dapeng\\"+file_folder+"\\chaolv" + str(j+1) +"-"+str(i+1)+ ".jpg")
            #         # img = img[y:y+w,x:x+h]
            #         src_gray = cv2.cvtColor(danzhu, cv2.COLOR_BGR2GRAY)
            #         the, thresh = cv2.threshold(src_gray, 0, 255, cv2.THRESH_OTSU)
            #         num_labels, labels, stats, centroids = cv2.connectedComponentsWithStats(thresh, connectivity=8)
            #         danzhu_img = np.zeros((danzhu.shape[0], danzhu.shape[1], 3), np.uint8)
            #         # 查看各个返回值
            """
            num_labels : 返回值是连通区域的数量。
            labels : labels是一个与image一样大小的矩形（labels.shape = image.shape），其中每一个连通区域会有一个唯一标识，标识从0开始。
            stats ：stats会包含5个参数分别为x,y,h,w,s。分别对应每一个连通区域的外接矩形的起始坐标x,y；外接矩形的wide,height；s其实不是外接矩形的面积，实践证明是labels对应的连通区域的像素个数。
            centroids : 返回的是连通区域的质心。
            """
    #         for p in range(1, num_labels):
    #             pexils = stats[p][4]
    #             if pexils <= 50:
    #                 continue
    #             mask = labels == p
    #             danzhu_img[:, :, 0][mask] = danzhu[:, :, 0][mask]
    #             danzhu_img[:, :, 1][mask] = danzhu[:, :, 1][mask]
    #             danzhu_img[:, :, 2][mask] = danzhu[:, :, 2][mask]
    #         cv2.imwrite("C:\\Users\\hzaurobot_1\\Desktop\\2.26dapeng\\" + file_folder + "\\quzao\\chaolv" + str(j + 1) + "-" + str(
    #             i + 1) + ".jpg", danzhu_img)
    #         pixel = 0
    #         all = danzhu_img.shape[0] * danzhu_img.shape[1]
    #         for m in range(danzhu_img.shape[1]):
    #             for n in range(danzhu_img.shape[0]):
    #                 if danzhu_img[n, m, 0] != 0 or danzhu_img[n, m, 1] != 0 or danzhu_img[n, m, 2] != 0:
    #                     pixel = pixel + 1
    #         print(all)
    #         print("有色像素占比" + str((pixel / all) * 100) + "%")
    #         print("像素投影统计值："+str(pixel))
    #         No_location = (0,0)
    #         if (pixel / all) * 100 == 0:
    #             No_Emergence_Plug_Seeding = No_Emergence_Plug_Seeding + 1
    #             No_location = (j + 1, i + 1)
    #             No_Emergence_Plug_Seeding_Location.append(No_location)
    #             transverse1_ptStart = (x[i], y[j])
    #             transverse1_ptEnd = (x[i + 1], y[j])
    #             transverse2_ptStart = (x[i], y[j + 1])
    #             transverse2_ptEnd = (x[i + 1], y[j + 1])
    #             machine1_ptStart = (x[i], y[j])
    #             machine1_ptEnd = (x[i], y[j + 1])
    #             machine2_ptStart = (x[i + 1], y[j])
    #             machine2_ptEnd = (x[i + 1], y[j + 1])
    #             cv2.line(yuantu, transverse1_ptStart, transverse1_ptEnd, point_color, thickness, lineType)
    #             cv2.line(yuantu, transverse2_ptStart, transverse2_ptEnd, point_color, thickness, lineType)
    #             cv2.line(yuantu, machine1_ptStart, machine1_ptEnd, point_color, thickness, lineType)
    #             cv2.line(yuantu, machine2_ptStart, machine2_ptEnd, point_color, thickness, lineType)
    print(No_Emergence_Plug_Seeding_Location)
    print("穴盘总穴孔数：" + str(All_Plug_Seeding))
    print("出苗穴孔数：" + str(All_Plug_Seeding - No_Emergence_Plug_Seeding))
    print("未出苗穴孔数：" + str(No_Emergence_Plug_Seeding))
    print("出苗率：" + str((All_Plug_Seeding - No_Emergence_Plug_Seeding) / All_Plug_Seeding * 100) + "%")
    print("补苗位置：")
    for i in range(len(No_Emergence_Plug_Seeding_Location)):
        print(str(No_Emergence_Plug_Seeding_Location[i][0]) + "-" + str(No_Emergence_Plug_Seeding_Location[i][1]))

    '''
    将检测结果写入并保存csv文件
    '''
    import datetime
    import csv
    now = datetime.datetime.now()
    detect_time = now.strftime("%Y-%m-%d %H:%M:%S")
    Seedling_plate_serial_number = "1-1盘"
    Emergence_rate = str((All_Plug_Seeding - No_Emergence_Plug_Seeding) / All_Plug_Seeding * 100) + "%"
    Weak_seedlings_location = No_Emergence_Plug_Seeding_Location
    No_seedlings_location = No_Emergence_Plug_Seeding_Location
    Amount_of_seedlings = len(No_Emergence_Plug_Seeding_Location)
    with open(csv_file_folder + "\\" + file_name + "_detcet.csv", "w", newline='') as csvfile:
        writer = csv.writer(csvfile)
        # 先写入columns_name
        writer.writerow(["检测时间", "苗盘序号", "出苗率", "弱苗位置", "无苗位置", "补苗量"])
        # 写入多行用writerows
        writer.writerows([[detect_time, Seedling_plate_serial_number, Emergence_rate, Weak_seedlings_location,
                           No_seedlings_location, Amount_of_seedlings]])

    '''
    cv2.imwrite为保存检测图片命令
    '''
    # cv2.namedWindow("image")
    # cv2.imshow('image', yuantu)
    # cv2.imwrite(file_folder+"\\"+file_name+"_chaolv.png",chaolv_rgb)
    cv2.imwrite(file_folder + "\\" + file_name + "_seed_detect.png", blade_detect)
    cv2.waitKey()
    cv2.destroyAllWindows()


if __name__ == '__main__':
    a = []
    print("ssssssssss")
    for i in range(1, len(sys.argv)):
        a.append((sys.argv[i]))
    detectLeaf(a[0], a[1], a[2])
