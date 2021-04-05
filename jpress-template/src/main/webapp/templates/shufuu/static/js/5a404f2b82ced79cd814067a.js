
          jQuery(".fullSlide").slide({ titCell:".hd li", mainCell:".bd ul", effect:"fold",  autoPlay:true, delayTime:700 });
      

    $(function() {
    // 注册栏默认值清空
    $(".g_ser input").focus(function() {
        if ($(this).val() == this.defaultValue) {
          $(this).val("");
        };
      }).blur(function() {
        if ($(this).val() == "") {
          $(this).val(this.defaultValue);
        };
      });
   })


       var key = document.getElementById("key");

            function searchInfo() {
                var base = $('head').data('base');
                if (key.value) {
                    location.href = base + "search.php?key=" + key.value;
                } else {
                    alert('请输入您要搜索的关键词！');
                }
            }
            key.addEventListener('keypress', function(event) {
                var keycode = event.keycode || event.which;
                if (keycode == "13") {
                    searchInfo();
                }
            });

        function searchLink(el) {
            var href = $(el).attr("href");
            location.href = href ? href : "/search.php?key=" + $(el).html();
        }
    

      $(".g_fa").slide({
          titCell: ".fahd  li",
          mainCell: ".fabd",
          autoPlay: false,
          titOnClassName: "on",
          effect: "fold",
          vis: 1
      });


      $(".m_hotcp").slide({
          titCell: ".hotcphd  li",
          mainCell: ".hotcpbd",
          autoPlay: false,
          titOnClassName: "on",
          effect: "fold",
          vis: 1
      });


      $(".g_ys").slide({
          titCell: ".m_yshd  li",
          mainCell: ".m_ysbd",
          autoPlay: false,
          titOnClassName: "on",
          effect: "fold",
          vis: 1
      });


      jQuery(".live-content").slide({ mainCell:".azcon ul",vis:3,prevCell:".u_ljt",nextCell:".u_rjt",autoPlay:true,effect:"leftLoop"});
    

      jQuery(".honor-content").slide({ mainCell:".zscon ul",vis:1,prevCell:".u_ljt",nextCell:".u_rjt",autoPlay:true,effect:"leftLoop"});
    

      jQuery(".g_news").slide({mainCell:".m_wd",autoPlay:true,effect:"topMarquee",vis:1,interTime:50,trigger:"click"});
    

    if (!(/msie [6|7|8|9]/i.test(navigator.userAgent))){
        (function(){
            window.scrollReveal = new scrollReveal({reset: true});
        })();
    };


        $(function() {
            var time;
            //var winHeight = top.window.document.body.clientHeight || $(window.parent).height();
            $('.client-2').css({
                'marginTop': -($('.client-2').height() / 2)
            });
            $('#client-2 li').on({
                'mouseenter': function() {
                    var scope=this;
                    time = setTimeout(function() {
                        var divDom = $(scope).children('div');
                        var maxWidth = divDom.width();
                        $(scope).stop().animate({
                            left: 77-maxWidth
                        }, 'normal', function() {
                            var pic = $(scope).find('.my-kefu-weixin-pic');
                            if (pic.length > 0) {
                                pic.show();
                            }
                        });
                    }, 100)
                },
                'mouseleave': function() {
                    var pic = $(this).find('.my-kefu-weixin-pic');
                    var divDom = $(this).children('div');
                    var maxWidth = divDom.width();
                    if (pic.length > 0) {
                        pic.hide();
                    }
                    clearTimeout(time);
                    var divDom = $(this).children('div');
                    $(this).stop().animate({
                        left: 0
                    }, "normal", function() {});
                }
            });
            //返回顶部
            $(window).scroll(function() {
                var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
                var eltop = $("#client-2").find(".my-kefu-ftop");
                if (scrollTop > 0) {
                    eltop.show();
                } else {
                    eltop.hide();
                }
            });
            $("#client-2").find(".my-kefu-ftop").click(function() {
                var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
                if (scrollTop > 0) {
                    $("html,body").animate({
                        scrollTop: 0
                    }, "slow");
                }
            });
        });
    
