$(function(){
          
  window.onresize = function(){
    if($(".sear-key").css('display') === 'none'){
      $(".se-keys").addClass("se-ul");                 
    }else{
      $(".se-keys").removeClass("se-ul");
    }
  }
  $(".serinp").click(function(){
    $(".se-ul").stop().slideToggle(200);
  });
  
})