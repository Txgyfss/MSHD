<template>
  <div>
    <div>
      <div class="main-body-content">
        <div class="main-body-search">
          <div class="main-body-header">管理员</div>
          <el-input size="small" class="main-input" placeholder="请输入用户名" v-model="search.username"></el-input>
          <el-button size="small" type="info" @click="load()">查询</el-button>
          <el-button size="small" type="primary" @click="add()">新增</el-button>
        </div>
        <div class="main-table-box">
          <el-table style="width: 100%" :data="tableData" size="small" strip header-cell-class-name="tableHeaderClass" >
						<el-table-column label="用户名" prop="username"></el-table-column>
						<el-table-column label="头像">
							<template v-slot="scope">
								<el-image :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
							</template>
						</el-table-column>
						<el-table-column label="手机号" prop="phone"></el-table-column>
						<el-table-column label="性别" prop="sex"></el-table-column>
            <el-table-column label="操作" :width="colWidth" fixed="right">
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
    <el-dialog title="请填写信息" :visible.sync="dialogVisible" width="40%">
      <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">
        <el-form-item label="头像">
          <el-upload action="http://localhost:8080/files/upload" ref="tx" :on-success="txSuccessUpload">
            <el-button size="small" type="success">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户名">
					<el-input size="small" v-model="form.username" placeholder="请输入用户名"></el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input size="small" v-model="form.phone" placeholder="请输入手机号"></el-input>
				</el-form-item>
				<el-form-item label="性别">
					<el-radio-group size="small" v-model="form.sex">
						<el-radio label="男"></el-radio>
						<el-radio label="女"></el-radio>
					</el-radio-group>
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


// 管理员模块接口请求统一前缀
let baseUrl = "/admin/"


export default {
  data() {
    return {
      user: {},
      colWidth: 150,
      tableData: [],
      pageNum: 1,
      dialogVisible: false,
      search: {},
      form: {

      },
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
			this.$nextTick(() => {
				this.$refs.tx.clearFiles()
			});

      this.dialogVisible = true;
    },
    // 编辑初始化模态框
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row));
			this.$nextTick(() => {
				this.$refs.tx.clearFiles()
			});

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
		txSuccessUpload(res) {
			this.form.avatar = res.data;
		},

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
  }
}
</script>
