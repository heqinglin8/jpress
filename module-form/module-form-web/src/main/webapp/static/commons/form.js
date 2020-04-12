function initInfoComponent() {

    $('#jpress-info-form').on('submit', function () {
        $(this).ajaxSubmit({
            type: "post",
            success: function (data) {
                if (data.state == "ok") {

                    alert('提交成功，我们将很快跟您联系，免费根据你的需求报价！');
                    location.reload();
                }
                //评论失败
                else {
                    alert('提交失败：' + data.message);
                }
            },
            error: function () {
                alert("网络错误，请稍后重试");
            }
        });
        return false;
    });

}

function initSubmitCount() {

    var str = '2020-04-12'.replace(/-/g,'/'); // 将-替换成/，因为下面这个构造函数只支持/分隔的日期字符串
    var date = new Date(str); // 构造一个日期型数据，值为传入的字符串
    var dateCount = Math.floor((new Date().getTime() - date.getTime())/1000/3600/24);
    var count =  dateCount * 8 + 1000;  //一天新增8单
    document.getElementById('jpress-submit-text').innerHTML = '目前已有 <span style="font-size: 1.4rem;color: #eea236;font-weight: 600">'+count+'</span> 人申请成功'

}



$(document).ready(function(){

    initInfoComponent();

    initSubmitCount()

});