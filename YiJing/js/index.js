function mouseove(currentNode) {
    var siblingss = []; //用来存放其他的兄弟节点
    var elseLi = currentNode.parentNode.children;
    for (var i = 0, elseLil = elseLi.length; i < elseLil; i++) {
        if (elseLi[i] !== currentNode) {//判断是否是ele节点，是否与触发事件的节点相同
            siblingss.push(elseLi[i]);
        }
        
    }
    for(var j=0;j<siblingss.length;j++){
        siblingss[j].classList.add("change");
    }

}

function mouseout(currentNode) {
    var siblingss = []; //用来存放其他的兄弟节点
    var elseLi = currentNode.parentNode.children;
    for (var i = 0, elseLil = elseLi.length; i < elseLil; i++) {
        if (elseLi[i] !== currentNode) {//判断是否是ele节点，是否与触发事件的节点相同
            siblingss.push(elseLi[i]);
        }
        
    }
    for(var j=0;j<siblingss.length;j++){
        siblingss[j].classList.remove("change");
    }

}
var status=1;
document.querySelectorAll('.index')[0].onclick=()=>{


    if(status==1){
        document.getElementById("nav").style.opacity=1;
        document.getElementById("nav").style.height="400px";
        status=0;
    }else{
        document.getElementById("nav").style.opacity=0;
        document.getElementById("nav").style.height="0";
        status=1;

    }
}