<template>
  <div>
    <div>
      <div class="main-body-content">
        <div class="main-body-search">
          <div class="main-body-header">评论管理</div>
          <el-input size="small" class="main-input" placeholder="请输入评论内容" v-model="search.content"></el-input>
          <el-button size="small" type="info" @click="load()">查询</el-button>
          <el-button size="small" type="primary" @click="add()">新增</el-button>
        </div>
        <div class="main-table-box">
          <el-table style="width: 100%" :data="tableData" size="small" strip header-cell-class-name="tableHeaderClass" >
						<el-table-column label="商品" prop="goodName"></el-table-column>
						<el-table-column label="评论内容" prop="content"></el-table-column>
						<el-table-column label="评论人" prop="userName"></el-table-column>
						<el-table-column label="评论时间" prop="time"></el-table-column>
            <el-table-column label="操作" :width="colWidth" fixed="right">
              <template v-slot="scope">
								<el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
									<el-button size="small" type="danger" slot="reference">删除</el-button>
								</el-popconfirm>

              </template>
            </el-table-column>
          </el-table>
        </div>
        <div style="margin: 20px 0">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-size="5"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";


// 商品分类模块接口请求统一前缀
let baseUrl = "/comment/"


export default {
  data() {
    return {
      user: {},
      colWidth: 150,
      tableData: [],
      pageNum: 1,
      dialogVisible: false,
      search: {},
      form: {},
      total: 0,

    }
  },
  // 页面加载的时候，做一些事情，在created里面
  mounted() {
    this.load()
  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
    operationSpace() {
      let width = 0
      let _this = this
      setTimeout(() => {
        let rows = document.getElementsByClassName('el-table__row');
        if (rows && rows.length) {
          let tds = rows[0].getElementsByTagName('td')
          let btns = tds[tds.length - 1].getElementsByTagName('button')
          for (let i = 0; i <btns.length; i++) {
            width += btns[i].offsetWidth + 5
          }
          _this.colWidth = width
        }
      }, 0)
    },
    // 分页加载表格数据
    load() {

      request.post(baseUrl + "page?pageNum=" + this.pageNum, this.search).then(res => {
        if (res.code === '0') {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.operationSpace();
        } else {
          this.$notify.error(res.msg);
        }
      });
    },
    // 新增初始化对话框
    add() {
      this.form = {};

      this.dialogVisible = true;
    },
    // 编辑初始化模态框
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row));

      this.dialogVisible = true;
    },

    save() {

      if (!this.form.id) {
        // 没有ID，说明是新增操作
        request.post(baseUrl, this.form).then(res => {
          if (res.code === '0') {
            this.$notify.success("添加成功");
            this.dialogVisible = false;

            this.load();
          } else {
            this.$notify.error(res.msg);
          }
        })
      } else {
        // 有ID，说明是更新操作
        request.put(baseUrl, this.form).then(res => {
          if (res.code === '0') {
            this.$notify.success('更新成功');
            this.dialogVisible = false;

            this.load();
          } else {
            this.$notify.error(res.msg);
          }
        })
      }
    },
    // 根据ID删除某条数据
    del(id) {
      request.delete(baseUrl + id).then(res => {
        if (res.code === '0') {
          this.$notify.success('删除成功');
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      });
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
  }
}
</script>
