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



$(document).ready(function(){

    initInfoComponent();

});