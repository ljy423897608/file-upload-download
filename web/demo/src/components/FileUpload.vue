<template>
  <div id="Count">
    <div class="Count_Top">
      <label >测试</label><br>
    </div>
    <div>
      <input type="file" value="" ref="myfile" id="file" style="display: none" @change='onUpload'>
      <mt-button type="default" @click.native="openFileUpload">上传</mt-button>
      <mt-progress v-model="progress" :bar-height="5">
        <div slot="end">{{progressShow}}</div>
      </mt-progress>
      <mt-button type="primary" @click.native="getRetUrlView">下载</mt-button>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue';
  import { Toast, Progress } from 'mint-ui';
  Vue.component(Progress.name, Progress);
  export default {
    name: 'test',
    data() {
      return {
        progress:0,
        progressShow:''
      }
    },
    methods: {
      getRetUrlView: function() {
		    //demo拿本地文件测试
        let filePath = 'test.jpeg'
        let fileName = 'test.jpeg'
        // console.log(JSON.parse(filePath).file.name)
        // console.log(JSON.parse(filePath).file.response.message)
        let entity = {
          filePath: filePath
        }
        let config = {
          responseType:'blob',
          onDownloadProgress:pre=>{
			//本人较懒，只打印下载数据，ui就不处理了。。。你懂的
            console.log(pre)
          }

        }
        this.$ajax.post('http://localhost:8081/media/downloadFile', entity,config)
          .then((response) => {
            this.download(response, fileName)
          })
      },
      // 下载文件
      download(d_data, fileName) {
        console.log(d_data)
        let name = ''
        if (fileName) {
          name = fileName
        }
        // alert(name)
        if (!d_data) {
          return
        }
        if (window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(d_data.data, fileName);
        } else {
          let url = window.URL.createObjectURL(new Blob([d_data.data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          // 获取文件名
          // download 属性定义了下载链接的地址而不是跳转路径
          link.setAttribute('download', name)
          document.body.appendChild(link)
          link.click()
          // alert(url)
        }
      },
      openFileUpload(){
        this.$refs.myfile.click();
      },
      resetFileInput() {
        this.$refs.myfile.value = '';
        this.refreshProgress(0);
      },
      refreshProgress(complete) {
        this.progress = complete;
        this.progressShow = complete == 0 ? '' : complete + "%";
      },
      onUpload(e){
        var formData = new FormData();
        formData.append('file', e.target.files[0]);
        console.log(1,e.target.files[0])
        var config = {
          onUploadProgress: progressEvent => {
            console.log(progressEvent)
            var complete = (progressEvent.loaded / progressEvent.total * 100 | 0);
            this.refreshProgress(complete);
          }
        }
        this.$ajax.post('http://localhost:8081/media/upload', formData, config).then(response => {
          Toast(response.data.note);
          this.resetFileInput();
        }, response => {
          console.info(response);
          Toast("上传失败[" + response + "]");
          this.resetFileInput();
        });
      }
    },
    created(){

    }
  };
</script>
<style scoped>
  #Count .Count_Top{
    padding: 16px;
    color: #26a2ff;
  }
  #Count .Count_Top label{
    font-size: 12px;
  }
  #Count .Count_Top{
    font-size: 30px;
  }
  #Count{
    margin-top: 0;
    position: relative;
    height: auto;
    background-color: #eee;
  }
</style>
