<template>
  <div>
    <div>
      <div class="main-body-content">
        <div class="main-body-search">
          <div class="main-body-header">热门商品管理</div>
          <el-input size="small" class="main-input" placeholder="请输入商品名称" v-model="search.name"></el-input>
					<el-button size="small" type="info" @click="load()">查询</el-button>
					<el-button size="small" type="primary" @click="add">新增</el-button>

        </div>
        <div class="main-table-box">
          <el-table style="width: 100%" :data="tableData" size="small" strip header-cell-class-name="tableHeaderClass" >
						<el-table-column label="商品图片">
							<template v-slot="scope">
								<el-image :src="scope.row.img" :preview-src-list="[scope.row.img]" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
							</template>
						</el-table-column>
						<el-table-column label="商品名称" prop="name"></el-table-column>
						<el-table-column label="商品分类" prop="type"></el-table-column>
						<el-table-column label="商品地区" prop="location"></el-table-column>
						<el-table-column label="商品价格" prop="price"></el-table-column>
						<el-table-column label="商品库存" prop="goodsnum"></el-table-column>
						<el-table-column label="是否热门" prop="ishot"></el-table-column>
						<el-table-column label="商品介绍">
							<template v-slot="scope">
								<el-button size="small" type="primary" @click="initEditorView(scope.row)">查看</el-button>
							</template>
						</el-table-column>
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
				<el-form-item label="商品图片">
					<el-upload action="http://localhost:8080/files/upload" ref="jdtp" :on-success="jdtpSuccessUpload">
						<el-button size="small" type="success">点击上传</el-button>
					</el-upload>
				</el-form-item>
				<el-form-item label="商品名称">
					<el-input size="small" v-model="form.name" placeholder="请输入商品名称"></el-input>
				</el-form-item>
				<el-form-item label="商品分类">
					<el-select size="small" style="width: 100%" v-model="form.typeId">
						<el-option v-for="item in typeData" :value="item.id" :label="item.name" :key="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="商品地区">
					<el-input size="small" v-model="form.location" placeholder="请输入商品地区"></el-input>
				</el-form-item>
				<el-form-item label="商品价格">
					<el-input size="small" v-model="form.price" placeholder="请输入商品价格"></el-input>
				</el-form-item>
        <el-form-item label="商品库存">
          <el-input size="small" v-model="form.goodsnum" placeholder="请输入商品库存"></el-input>
        </el-form-item>
				<el-form-item label="商品介绍">
					<div id="wang-editor"></div>
				</el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
          <el-button size="small" @click="dialogVisible = false">取 消</el-button>
          <el-button size="small" type="primary" @click="save">保 存</el-button>
      </div>
    </el-dialog>
	<el-dialog title="商品介绍" :visible.sync="richTextDialogVisible">
		<div class="w-e-text" v-html="form.description"></div>
	</el-dialog>

  </div>
</template>

<script>
import request from "@/utils/request";
import wangEditor from "wangeditor";

// 商品管理模块接口请求统一前缀
let baseUrl = "/goods/"

// 富文本
let editor
function initWangEditor(content) {	setTimeout(() => {
		if (!editor) {
			editor = new wangEditor('#wang-editor')
			editor.config.placeholder = '请输入内容'
			editor.config.uploadFileName = 'file'
			editor.config.uploadImgServer = 'http://localhost:8080/files/wang/upload'
			editor.create()
		}
		editor.txt.html(content)
	}, 0)
}
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
      typeData: [],
			richTextDialogVisible: false,

    }
  },
  // 页面加载的时候，做一些事情，在created里面
  mounted() {
		// 加载商品分类
		this.loadType();
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

      request.post("/goods/hot/" + "page?pageNum=" + this.pageNum, this.search).then(res => {
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
			initWangEditor('');
			this.$nextTick(() => {
				this.$refs.jdtp.clearFiles()
			});

      this.dialogVisible = true;
    },
    // 编辑初始化模态框
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row));
			initWangEditor(row.description);
      this.$nextTick(() => {
				this.$refs.jdtp.clearFiles()
			});

      this.dialogVisible = true;
    },
		initEditorView(o) {
			this.form = JSON.parse(JSON.stringify(o));
			this.richTextDialogVisible = true;
		},

    save() {
			this.form.description = editor.txt.html();
			//在热销模块新增商品 默认是热销
			this.form.ishot = "是";
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
		jdtpSuccessUpload(res) {
			this.form.img = res.data;
		},
		// 加载商品分类
    loadType() {
			request.get("/type").then(res => {
				if (res.code === '0') {
					this.typeData = res.data;
				} else {
					this.$notify.error(res.msg);
				}
			})
		},

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
  }
}
</script>
