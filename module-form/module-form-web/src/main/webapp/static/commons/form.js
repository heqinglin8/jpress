function initInfoComponent() {

    $('#jpress-info-form').on('submit', function () {
        $(this).ajaxSubmit({
            type: "post",
            success: function (data) {
                alert('111111');
                if (data.state == "ok") {

                    alert('提交成功，将很快给你回复！');
                    location.reload();
                }
                //评论失败
                else {
                    alert('提交失败，' + data.message);
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