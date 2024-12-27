<template>
  <div>
    <div>
      <div class="main-body-content">
        <div class="main-body-search">
          <div class="main-body-header">灾情数据表格</div>
          <el-input size="small" class="main-input" placeholder="请输入查询条件" v-model="searchQuery"></el-input>
          <el-button size="small" type="info" @click="load">查询</el-button>
        </div>
        <div class="main-table-box">
          <el-table style="width: 100%" :data="tableData" size="small" stripe header-cell-class-name="tableHeaderClass">
            <el-table-column label="灾情码" prop="disastercode"></el-table-column>
            <el-table-column label="省份" prop="province"></el-table-column>
            <el-table-column label="城市" prop="city"></el-table-column>
            <el-table-column label="区/县" prop="country"></el-table-column>
            <el-table-column label="乡镇" prop="town"></el-table-column>
            <el-table-column label="村" prop="village"></el-table-column>
            <el-table-column label="日期" prop="date"></el-table-column>
            <el-table-column label="数据来源" prop="sourceCategory"></el-table-column>
            <el-table-column label="灾情类型" prop="disasterMajorCategory"></el-table-column>
            <el-table-column label="操作" :width="150" fixed="right">
              <template v-slot="scope">
                <el-button size="small" type="primary" @click="edit(scope.row)">编辑</el-button>
                <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
                  <el-button size="small" type="danger" slot="reference">删除</el-button>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div style="margin: 20px 0">
          <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum" :page-size="5"
            layout="total, prev, pager, next" :total="total"></el-pagination>
        </div>
      </div>
    </div>
    <el-dialog title="编辑灾情信息" :visible.sync="dialogVisible" width="40%">
      <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">
        <el-form-item label="ID">
          <el-input size="small" v-model="form.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="灾情码">
          <el-input size="small" v-model="form.disastercode" disabled></el-input>
        </el-form-item>
        <el-form-item label="省份">
          <el-input size="small" v-model="form.province"></el-input>
        </el-form-item>
        <el-form-item label="城市">
          <el-input size="small" v-model="form.city"></el-input>
        </el-form-item>
        <el-form-item label="区/县">
          <el-input size="small" v-model="form.country"></el-input>
        </el-form-item>
        <el-form-item label="乡镇">
          <el-input size="small" v-model="form.town"></el-input>
        </el-form-item>
        <el-form-item label="村">
          <el-input size="small" v-model="form.village"></el-input>
        </el-form-item>
        <el-form-item label="日期">
          <el-input size="small" v-model="form.date"></el-input>
        </el-form-item>
        <el-form-item label="数据来源">
          <el-input size="small" v-model="form.sourceCategory"></el-input>
        </el-form-item>
        <el-form-item label="详情">
          <el-input size="small" v-model="form.detail"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="save">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {
      colWidth: 150,
      tableData: [],
      pageNum: 1,
      dialogVisible: false,
      searchQuery: "",
      form: {},
      total: 0,
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    // 加载表格数据
    load() {
      const params = {
        pageNum: this.pageNum,
        pageSize: 5,
        query: this.searchQuery,
      };
      request.get("/all", { params }).then((res) => {
        if (res.code === "0") {
          this.tableData = res.data;
          this.total = res.data.length;
        } else {
          this.$notify.error(res.msg);
        }
      });
    },
    // 编辑数据
    edit(row) {
      // 将行数据赋值给表单，并格式化日期字段为 'yyyy-MM-ddTHH:mm:ss' 格式
      this.form = {
        ...row,
        date: row.date ? new Date(row.date).toISOString().split(".")[0] : null, // 格式化日期
      };
      this.dialogVisible = true;
    },

    // 保存数据
    save() {
      // 如果日期为空，则不发送该字段
      const payload = {
        ...this.form,
        date: this.form.date ? new Date(this.form.date).toISOString().split(".")[0] : null, // 再次格式化日期，防止用户修改后格式错误
      };

      request.post("http://localhost:8080/update", payload).then((res) => {
        if (res.code === "0") {
          this.$notify.success("保存成功");
          this.dialogVisible = false;
          this.load(); // 刷新表格数据
        } else {
          this.$notify.error(res.msg);
        }
      });
    },

    // 删除数据
    del(id) {
      console.log("删除的 ID 是：", id);
      if (!id) {
        this.$notify.error("ID不能为空");
        return;
      }

      request
        .delete(`http://localhost:8080/delete/${id}`)
        .then((res) => {
          if (res.code === "0") {
            this.$notify.success("删除成功");
            this.load(); // 刷新表格数据
          } else {
            this.$notify.error(res.msg);
          }
        })
        .catch((err) => {
          console.error("删除失败：", err);
          this.$notify.error("删除操作失败，请稍后重试");
        });
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
  },
};
</script>
