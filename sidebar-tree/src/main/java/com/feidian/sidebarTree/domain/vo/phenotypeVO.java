package com.feidian.sidebarTree.domain.vo;

public class phenotypeVO {
    private Long id;
    // 物种
    private String species;

    // 群体
    private String population;

    // 年份
    private int year;

    // 地区
    private String region;

    // 重复试验
    private boolean repeatExperiment;

    // 品种ID
    private String varietyId;

    // 品种名称
    private String varietyName;

    // 材料名称
    private String materialName;

    // 田间编号
    private String fieldNumber;

    // 对照类型
    private String controlType;

    // 父本
    private String maleParent;

    // 母本
    private String femaleParent;

    // 含水量
    private double moistureContent;

    // 亩产
    private double yieldPerAcre;

    // 小区籽粒鲜重
    private double freshWeightOfGrains;

    // 倒伏率
    private double lodgingRate;

    // 倒折率
    private double breakingRate;

    // 穗位
    private int spikePosition;

    // 南方锈病（级）
    private int southernRustLevel;

    // 茎腐病（%）
    private double stemRotPercentage;

    // 大斑病（级）
    private int largeSpotLevel;

    // 小斑病（级）
    private int smallSpotLevel;

    // 弯孢叶斑病（级）
    private int bentLeafSpotLevel;

    // 褐斑病（级）
    private int brownSpotLevel;

    // 穗腐病（%）
    private double spikeRotPercentage;

    // 瘤黑粉病（%）
    private double smutPercentage;

    // 抽雄期
    private String maleFloweringStage;

    // 雄穗开花
    private String maleSpikeFlowering;

    // 雌穗开花
    private String femaleSpikeFlowering;

    // 株高
    private double plantHeight;

    // 穗位高
    private double spikePositionHeight;

    // 穗位叶长
    private double spikePositionLeafLength;

    // 穗位叶宽
    private double spikePositionLeafWidth;

    // 穗上茎节数
    private int spikeStemNodesAbove;

    // 穗下茎节数
    private int spikeStemNodesBelow;

    // 雄穗分枝数
    private int maleSpikeBranches;

    // 雄穗主支长度
    private double maleSpikeMainBranchLength;

    // 穗重
    private double spikeWeight;

    // 穗轴重
    private double spikeAxisWeight;

    // 穗粗
    private double spikeThickness;

    // 穗长
    private double spikeLength;

    // 穗行数
    private int spikeRows;

    // 每行籽粒数
    private int grainsPerRow;

    // 每穗总粒数
    private int totalGrainsPerSpike;

    // 穗粒总重
    private double totalGrainWeight;

    // 粒长
    private double grainLength;

    // 粒宽
    private double grainWidth;

    // 每升总粒数
    private int totalGrainsPerLiter;

    // 百粒重
    private double hundredGrainWeight;

    // 容重
    private double bulkDensity;

    // 籽粒周长
    private double grainCircumference;

    // 籽粒投影面积
    private double grainProjectionArea;

    // 备注
    private String remark;


    // Constructor, getters, and setters


    public phenotypeVO() {
    }

    public phenotypeVO(Long id , String species, String population, int year, String region, boolean repeatExperiment, String varietyId, String varietyName, String materialName, String fieldNumber, String controlType, String maleParent, String femaleParent, double moistureContent, double yieldPerAcre, double freshWeightOfGrains, double lodgingRate, double breakingRate, int spikePosition, int southernRustLevel, double stemRotPercentage, int largeSpotLevel, int smallSpotLevel, int bentLeafSpotLevel, int brownSpotLevel, double spikeRotPercentage, double smutPercentage, String maleFloweringStage, String maleSpikeFlowering, String femaleSpikeFlowering, double plantHeight, double spikePositionHeight, double spikePositionLeafLength, double spikePositionLeafWidth, int spikeStemNodesAbove, int spikeStemNodesBelow, int maleSpikeBranches, double maleSpikeMainBranchLength, double spikeWeight, double spikeAxisWeight, double spikeThickness, double spikeLength, int spikeRows, int grainsPerRow, int totalGrainsPerSpike, double totalGrainWeight, double grainLength, double grainWidth, int totalGrainsPerLiter, double hundredGrainWeight, double bulkDensity, double grainCircumference, double grainProjectionArea, String remark) {
        this.id = id;
        this.species = species;
        this.population = population;
        this.year = year;
        this.region = region;
        this.repeatExperiment = repeatExperiment;
        this.varietyId = varietyId;
        this.varietyName = varietyName;
        this.materialName = materialName;
        this.fieldNumber = fieldNumber;
        this.controlType = controlType;
        this.maleParent = maleParent;
        this.femaleParent = femaleParent;
        this.moistureContent = moistureContent;
        this.yieldPerAcre = yieldPerAcre;
        this.freshWeightOfGrains = freshWeightOfGrains;
        this.lodgingRate = lodgingRate;
        this.breakingRate = breakingRate;
        this.spikePosition = spikePosition;
        this.southernRustLevel = southernRustLevel;
        this.stemRotPercentage = stemRotPercentage;
        this.largeSpotLevel = largeSpotLevel;
        this.smallSpotLevel = smallSpotLevel;
        this.bentLeafSpotLevel = bentLeafSpotLevel;
        this.brownSpotLevel = brownSpotLevel;
        this.spikeRotPercentage = spikeRotPercentage;
        this.smutPercentage = smutPercentage;
        this.maleFloweringStage = maleFloweringStage;
        this.maleSpikeFlowering = maleSpikeFlowering;
        this.femaleSpikeFlowering = femaleSpikeFlowering;
        this.plantHeight = plantHeight;
        this.spikePositionHeight = spikePositionHeight;
        this.spikePositionLeafLength = spikePositionLeafLength;
        this.spikePositionLeafWidth = spikePositionLeafWidth;
        this.spikeStemNodesAbove = spikeStemNodesAbove;
        this.spikeStemNodesBelow = spikeStemNodesBelow;
        this.maleSpikeBranches = maleSpikeBranches;
        this.maleSpikeMainBranchLength = maleSpikeMainBranchLength;
        this.spikeWeight = spikeWeight;
        this.spikeAxisWeight = spikeAxisWeight;
        this.spikeThickness = spikeThickness;
        this.spikeLength = spikeLength;
        this.spikeRows = spikeRows;
        this.grainsPerRow = grainsPerRow;
        this.totalGrainsPerSpike = totalGrainsPerSpike;
        this.totalGrainWeight = totalGrainWeight;
        this.grainLength = grainLength;
        this.grainWidth = grainWidth;
        this.totalGrainsPerLiter = totalGrainsPerLiter;
        this.hundredGrainWeight = hundredGrainWeight;
        this.bulkDensity = bulkDensity;
        this.grainCircumference = grainCircumference;
        this.grainProjectionArea = grainProjectionArea;
        this.remark = remark;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isRepeatExperiment() {
        return repeatExperiment;
    }

    public void setRepeatExperiment(boolean repeatExperiment) {
        this.repeatExperiment = repeatExperiment;
    }

    public String getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(String varietyId) {
        this.varietyId = varietyId;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(String fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public String getMaleParent() {
        return maleParent;
    }

    public void setMaleParent(String maleParent) {
        this.maleParent = maleParent;
    }

    public String getFemaleParent() {
        return femaleParent;
    }

    public void setFemaleParent(String femaleParent) {
        this.femaleParent = femaleParent;
    }

    public double getMoistureContent() {
        return moistureContent;
    }

    public void setMoistureContent(double moistureContent) {
        this.moistureContent = moistureContent;
    }

    public double getYieldPerAcre() {
        return yieldPerAcre;
    }

    public void setYieldPerAcre(double yieldPerAcre) {
        this.yieldPerAcre = yieldPerAcre;
    }

    public double getFreshWeightOfGrains() {
        return freshWeightOfGrains;
    }

    public void setFreshWeightOfGrains(double freshWeightOfGrains) {
        this.freshWeightOfGrains = freshWeightOfGrains;
    }

    public double getLodgingRate() {
        return lodgingRate;
    }

    public void setLodgingRate(double lodgingRate) {
        this.lodgingRate = lodgingRate;
    }

    public double getBreakingRate() {
        return breakingRate;
    }

    public void setBreakingRate(double breakingRate) {
        this.breakingRate = breakingRate;
    }

    public int getSpikePosition() {
        return spikePosition;
    }

    public void setSpikePosition(int spikePosition) {
        this.spikePosition = spikePosition;
    }

    public int getSouthernRustLevel() {
        return southernRustLevel;
    }

    public void setSouthernRustLevel(int southernRustLevel) {
        this.southernRustLevel = southernRustLevel;
    }

    public double getStemRotPercentage() {
        return stemRotPercentage;
    }

    public void setStemRotPercentage(double stemRotPercentage) {
        this.stemRotPercentage = stemRotPercentage;
    }

    public int getLargeSpotLevel() {
        return largeSpotLevel;
    }

    public void setLargeSpotLevel(int largeSpotLevel) {
        this.largeSpotLevel = largeSpotLevel;
    }

    public int getSmallSpotLevel() {
        return smallSpotLevel;
    }

    public void setSmallSpotLevel(int smallSpotLevel) {
        this.smallSpotLevel = smallSpotLevel;
    }

    public int getBentLeafSpotLevel() {
        return bentLeafSpotLevel;
    }

    public void setBentLeafSpotLevel(int bentLeafSpotLevel) {
        this.bentLeafSpotLevel = bentLeafSpotLevel;
    }

    public int getBrownSpotLevel() {
        return brownSpotLevel;
    }

    public void setBrownSpotLevel(int brownSpotLevel) {
        this.brownSpotLevel = brownSpotLevel;
    }

    public double getSpikeRotPercentage() {
        return spikeRotPercentage;
    }

    public void setSpikeRotPercentage(double spikeRotPercentage) {
        this.spikeRotPercentage = spikeRotPercentage;
    }

    public double getSmutPercentage() {
        return smutPercentage;
    }

    public void setSmutPercentage(double smutPercentage) {
        this.smutPercentage = smutPercentage;
    }

    public String getMaleFloweringStage() {
        return maleFloweringStage;
    }

    public void setMaleFloweringStage(String maleFloweringStage) {
        this.maleFloweringStage = maleFloweringStage;
    }

    public String getMaleSpikeFlowering() {
        return maleSpikeFlowering;
    }

    public void setMaleSpikeFlowering(String maleSpikeFlowering) {
        this.maleSpikeFlowering = maleSpikeFlowering;
    }

    public String getFemaleSpikeFlowering() {
        return femaleSpikeFlowering;
    }

    public void setFemaleSpikeFlowering(String femaleSpikeFlowering) {
        this.femaleSpikeFlowering = femaleSpikeFlowering;
    }

    public double getPlantHeight() {
        return plantHeight;
    }

    public void setPlantHeight(double plantHeight) {
        this.plantHeight = plantHeight;
    }

    public double getSpikePositionHeight() {
        return spikePositionHeight;
    }

    public void setSpikePositionHeight(double spikePositionHeight) {
        this.spikePositionHeight = spikePositionHeight;
    }

    public double getSpikePositionLeafLength() {
        return spikePositionLeafLength;
    }

    public void setSpikePositionLeafLength(double spikePositionLeafLength) {
        this.spikePositionLeafLength = spikePositionLeafLength;
    }

    public double getSpikePositionLeafWidth() {
        return spikePositionLeafWidth;
    }

    public void setSpikePositionLeafWidth(double spikePositionLeafWidth) {
        this.spikePositionLeafWidth = spikePositionLeafWidth;
    }

    public int getSpikeStemNodesAbove() {
        return spikeStemNodesAbove;
    }

    public void setSpikeStemNodesAbove(int spikeStemNodesAbove) {
        this.spikeStemNodesAbove = spikeStemNodesAbove;
    }

    public int getSpikeStemNodesBelow() {
        return spikeStemNodesBelow;
    }

    public void setSpikeStemNodesBelow(int spikeStemNodesBelow) {
        this.spikeStemNodesBelow = spikeStemNodesBelow;
    }

    public int getMaleSpikeBranches() {
        return maleSpikeBranches;
    }

    public void setMaleSpikeBranches(int maleSpikeBranches) {
        this.maleSpikeBranches = maleSpikeBranches;
    }

    public double getMaleSpikeMainBranchLength() {
        return maleSpikeMainBranchLength;
    }

    public void setMaleSpikeMainBranchLength(double maleSpikeMainBranchLength) {
        this.maleSpikeMainBranchLength = maleSpikeMainBranchLength;
    }

    public double getSpikeWeight() {
        return spikeWeight;
    }

    public void setSpikeWeight(double spikeWeight) {
        this.spikeWeight = spikeWeight;
    }

    public double getSpikeAxisWeight() {
        return spikeAxisWeight;
    }

    public void setSpikeAxisWeight(double spikeAxisWeight) {
        this.spikeAxisWeight = spikeAxisWeight;
    }

    public double getSpikeThickness() {
        return spikeThickness;
    }

    public void setSpikeThickness(double spikeThickness) {
        this.spikeThickness = spikeThickness;
    }

    public double getSpikeLength() {
        return spikeLength;
    }

    public void setSpikeLength(double spikeLength) {
        this.spikeLength = spikeLength;
    }

    public int getSpikeRows() {
        return spikeRows;
    }

    public void setSpikeRows(int spikeRows) {
        this.spikeRows = spikeRows;
    }

    public int getGrainsPerRow() {
        return grainsPerRow;
    }

    public void setGrainsPerRow(int grainsPerRow) {
        this.grainsPerRow = grainsPerRow;
    }

    public int getTotalGrainsPerSpike() {
        return totalGrainsPerSpike;
    }

    public void setTotalGrainsPerSpike(int totalGrainsPerSpike) {
        this.totalGrainsPerSpike = totalGrainsPerSpike;
    }

    public double getTotalGrainWeight() {
        return totalGrainWeight;
    }

    public void setTotalGrainWeight(double totalGrainWeight) {
        this.totalGrainWeight = totalGrainWeight;
    }

    public double getGrainLength() {
        return grainLength;
    }

    public void setGrainLength(double grainLength) {
        this.grainLength = grainLength;
    }

    public double getGrainWidth() {
        return grainWidth;
    }

    public void setGrainWidth(double grainWidth) {
        this.grainWidth = grainWidth;
    }

    public int getTotalGrainsPerLiter() {
        return totalGrainsPerLiter;
    }

    public void setTotalGrainsPerLiter(int totalGrainsPerLiter) {
        this.totalGrainsPerLiter = totalGrainsPerLiter;
    }

    public double getHundredGrainWeight() {
        return hundredGrainWeight;
    }

    public void setHundredGrainWeight(double hundredGrainWeight) {
        this.hundredGrainWeight = hundredGrainWeight;
    }

    public double getBulkDensity() {
        return bulkDensity;
    }

    public void setBulkDensity(double bulkDensity) {
        this.bulkDensity = bulkDensity;
    }

    public double getGrainCircumference() {
        return grainCircumference;
    }

    public void setGrainCircumference(double grainCircumference) {
        this.grainCircumference = grainCircumference;
    }

    public double getGrainProjectionArea() {
        return grainProjectionArea;
    }

    public void setGrainProjectionArea(double grainProjectionArea) {
        this.grainProjectionArea = grainProjectionArea;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
