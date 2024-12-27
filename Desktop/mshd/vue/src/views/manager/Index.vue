<template>
  <div>
    <div class="main-body-header">欢迎进入多源灾情数据管理服务系统</div>
    <div class="main-body-content">
      <div style="margin-bottom: 100px; width: 100%">
        <el-collapse v-model="activeName" accordion>
          <el-collapse-item v-for="item in notices" :key="item.id" :title="item.name" :name="item.id">
            <div style="padding: 0 20px">{{ item.content }}</div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div>
        <!-- 使用 el-row 和 el-col，将每行设置为四个图表 -->
        <el-row :gutter="20">
          <el-col :span="6">
            <div id="pieChart" style="height: 400px"></div>
          </el-col>
          <el-col :span="6">
            <div id="mapChart" style="height: 400px"></div>
          </el-col>
          <el-col :span="6">
            <div id="rankingChart" style="height: 400px"></div>
          </el-col>
          <el-col :span="6">
            <div id="barChart" style="height: 400px"></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <div id="lineChart" style="height: 400px"></div>
          </el-col>
          <el-col :span="6">
            <div id="stackedBarChart" style="height: 400px"></div>
          </el-col>
          <el-col :span="6">
            <div id="radarChart" style="height: 400px"></div>
          </el-col>
          <el-col :span="6">
            <div id="dataFormatChart" style="height: 400px"></div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>
import request from "@/utils/request";
import * as echarts from "echarts";
import "echarts/map/js/china";

export default {
  name: "IndexView",
  data() {
    return {
      activeName: "",
      notices: [],
      disasterData: [],
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      request.get("/all").then((res) => {
        if (res.code === "0") {
          this.disasterData = res.data;
          this.initCharts();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    initCharts() {
      this.initPieChart();
      this.initMapChart();
      this.initRankingChart();
      this.initBarChart();
      this.initLineChart();
      this.initStackedBarChart();
      this.initRadarChart();
      this.initDataFormatChart();
    },
    initPieChart() {
      const pieChart = echarts.init(document.getElementById("pieChart"));
      const categoryData = {};
      this.disasterData.forEach((item) => {
        const category = item.sourceCategory || "其他";
        categoryData[category] = (categoryData[category] || 0) + 1;
      });
      const chartData = Object.entries(categoryData).map(([name, value]) => ({
        name,
        value,
      }));

      pieChart.setOption({
        title: {
          text: "不同灾情大类信息占比",
          left: "center",
          textStyle: { color: "#003366", fontSize: 16 },
        },
        tooltip: { trigger: "item" },
        legend: {
          bottom: "3%",
          left: "center",
          textStyle: { color: "#003366", fontSize: 10 },
        },
        series: [
          {
            name: "灾情类型",
            type: "pie",
            radius: ["30%", "50%"],
            label: {
              formatter: "{b}\n{d}%",
              color: "#003366",
              fontSize: 8,
            },
            data: chartData,
            color: ["#5470c6", "#91cc75", "#fac858"],
          },
        ],
      });
    },
    initMapChart() {
      const mapChart = echarts.init(document.getElementById("mapChart"));

      // 定义一个映射表，将"北京市"等名称转换为ECharts内置地图可以识别的名称
      const provinceMapping = {
        北京市: "北京",
        天津市: "天津",
        上海市: "上海",
        重庆市: "重庆",
        河北省: "河北",
        山西省: "山西",
        辽宁省: "辽宁",
        吉林省: "吉林",
        黑龙江省: "黑龙江",
        江苏省: "江苏",
        浙江省: "浙江",
        安徽省: "安徽",
        福建省: "福建",
        江西省: "江西",
        山东省: "山东",
        河南省: "河南",
        湖北省: "湖北",
        湖南省: "湖南",
        广东省: "广东",
        海南省: "海南",
        四川省: "四川",
        贵州省: "贵州",
        云南省: "云南",
        陕西省: "陕西",
        甘肃省: "甘肃",
        青海省: "青海",
        台湾省: "台湾",
        内蒙古自治区: "内蒙古",
        广西壮族自治区: "广西",
        西藏自治区: "西藏",
        宁夏回族自治区: "宁夏",
        新疆维吾尔自治区: "新疆",
        香港特别行政区: "香港",
        澳门特别行政区: "澳门",
      };

      // 将原始数据转换为标准名称
      const provinceData = {};
      this.disasterData.forEach((item) => {
        const rawProvince = item.province || "未知";
        const province = provinceMapping[rawProvince] || rawProvince; // 映射到标准名称
        provinceData[province] = (provinceData[province] || 0) + 1;
      });

      console.log("映射后的省份数据：", provinceData);

      // 转换为 ECharts 可识别的格式
      const chartData = Object.entries(provinceData).map(([name, value]) => ({
        name,
        value,
      }));

      // 计算最大值，用于视觉映射
      const maxValue = Math.max(...chartData.map((d) => d.value));

      // 配置 ECharts 地图
      mapChart.setOption({
        title: {
          text: "各省灾情分布情况",
          left: "center",
          textStyle: { color: "#003366", fontSize: 16 },
        },
        tooltip: {
          trigger: "item",
          formatter: "{b}<br/>灾情数量: {c}",
        },
        visualMap: {
          min: 0,
          max: maxValue,
          left: "left",
          textStyle: { color: "#003366" },
          inRange: { color: ["#d7f0fa", "#005bb5"] },
        },
        geo: {
          map: "china",
          label: {
            emphasis: { show: false },
          },
          itemStyle: {
            normal: {
              areaColor: "#f9f9f9",
              borderColor: "#ccc",
            },
            emphasis: {
              areaColor: "#d1ecff",
            },
          },
        },
        series: [
          {
            name: "灾情数量",
            type: "map",
            map: "china",
            data: chartData,
          },
        ],
      });
    },

    initRankingChart() {
      const rankingChart = echarts.init(document.getElementById("rankingChart"));
      const cityData = {};
      this.disasterData.forEach((item) => {
        const city = item.city || "未知";
        cityData[city] = (cityData[city] || 0) + 1;
      });
      const sortedData = Object.entries(cityData)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 5); // 显示前五名

      rankingChart.setOption({
        title: {
          text: "按城市灾情数量排名",
          left: "center",
          textStyle: { color: "#003366", fontSize: 16 },
        },
        tooltip: { trigger: "axis" },
        yAxis: {
          type: "category",
          data: sortedData.map(([name]) => name),
          axisLabel: { color: "#003366", fontSize: 8 },
        },
        xAxis: { type: "value", axisLabel: { color: "#003366", fontSize: 8 } },
        series: [
          {
            type: "bar",
            data: sortedData.map(([, value]) => value),
            itemStyle: { color: "#40a9ff" },
          },
        ],
      });
    },
    initBarChart() {
      const barChart = echarts.init(document.getElementById("barChart"));
      const subCategoryData = {};
      this.disasterData.forEach((item) => {
        const subCategory = item.disasterSubCategory || "其他";
        subCategoryData[subCategory] =
          (subCategoryData[subCategory] || 0) + 1;
      });
      const chartData = Object.entries(subCategoryData);

      barChart.setOption({
        title: {
          text: "灾情类型分布",
          left: "center",
          textStyle: { color: "#003366", fontSize: 16 },
        },
        tooltip: { trigger: "axis" },
        xAxis: {
          type: "category",
          data: chartData.map(([name]) => name),
          axisLabel: { color: "#003366", fontSize: 7 },
        },
        yAxis: {
          type: "value",
          axisLabel: { color: "#003366", fontSize: 8 },
        },
        series: [
          {
            type: "bar",
            data: chartData.map(([, value]) => value),
            itemStyle: { color: "#91cc75" },
          },
        ],
      });
    },
    initLineChart() {
      const lineChart = echarts.init(document.getElementById("lineChart"));
      const timeData = {};
      this.disasterData.forEach((item) => {
        const date = item.date || "未知";
        timeData[date] = (timeData[date] || 0) + 1;
      });
      const sortedDates = Object.keys(timeData).sort();

      lineChart.setOption({
        title: {
          text: "灾情时段分布",
          left: "center",
          textStyle: { color: "#003366" },
        },
        tooltip: { trigger: "axis" },
        xAxis: {
          type: "category",
          data: sortedDates,
          axisLabel: { color: "#003366" },
        },
        yAxis: { type: "value", axisLabel: { color: "#003366" } },
        series: [
          {
            type: "line",
            data: sortedDates.map((date) => timeData[date]),
            itemStyle: { color: "#5470c6" },
          },
        ],
      });
    },
    initStackedBarChart() {
      const stackedBarChart = echarts.init(
        document.getElementById("stackedBarChart")
      );
      const stackedData = {};
      this.disasterData.forEach((item) => {
        const date = item.date || "未知";
        const type = item.disasterMajorCategory || "其他";
        if (!stackedData[date]) stackedData[date] = {};
        stackedData[date][type] =
          (stackedData[date][type] || 0) + 1;
      });
      const dates = Object.keys(stackedData).sort();
      const categories = [
        ...new Set(
          this.disasterData.map((item) => item.disasterMajorCategory || "其他")
        ),
      ];
      const series = categories.map((category) => ({
        name: category,
        type: "bar",
        stack: "总量",
        data: dates.map((date) => stackedData[date][category] || 0),
      }));

      stackedBarChart.setOption({
        title: { text: "灾情数量统计", left: "center" },
        tooltip: { trigger: "axis" },
        yAxis: { type: "category", data: dates },
        xAxis: { type: "value" },
        series,
      });
    },
    initRadarChart() {
      const radarChart = echarts.init(document.getElementById("radarChart"));
      const categoryData = {};
      this.disasterData.forEach((item) => {
        const category = item.disasterMajorCategory || "其他";
        categoryData[category] = (categoryData[category] || 0) + 1;
      });
      const indicators = Object.keys(categoryData).map((name) => ({
        name,
        max: Math.max(...Object.values(categoryData)) + 10,
      }));

      radarChart.setOption({
        title: { text: "灾情类型雷达分布", left: "center" },
        radar: { indicator: indicators },
        series: [
          {
            type: "radar",
            data: [
              {
                value: Object.values(categoryData),
                name: "灾情类型",
              },
            ],
          },
        ],
      });
    },
    initDataFormatChart() {
      const dataFormatChart = echarts.init(
        document.getElementById("dataFormatChart")
      );
      const formatData = {};
      this.disasterData.forEach((item) => {
        const format = item.carrierForm || "其他";
        formatData[format] = (formatData[format] || 0) + 1;
      });
      const chartData = Object.entries(formatData).map(([name, value]) => ({
        name,
        value,
      }));

      dataFormatChart.setOption({
        title: {
          text: "数据格式统计",
          left: "center",
          textStyle: { color: "#003366", fontSize: 16 },
        },
        tooltip: {
          trigger: "item",
          formatter: "{b}: {c}%",
        },
        series: [
          {
            type: "treemap",
            data: chartData,
            label: {
              show: true,
              formatter: "{b}\n{c}%",
              color: "#ffffff",
              fontSize: 12,
            },
            breadcrumb: { show: false },
            roam: false,
          },
        ],
      });
    },
  },
};
</script>
