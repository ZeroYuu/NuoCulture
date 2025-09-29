
var gu = document.getElementById('gu');
var jin = document.getElementById('jin');
var gu1 = document.getElementById('gu1');
var jin1 = document.getElementById('jin1');
document.getElementById("next").onclick = function() {
    gu .style.left =  '-1300px';
    jin.style.right =  '-1300px';
    gu.style.opacity="0.5";
    jin.style.opacity="0.5";
    document.getElementById('imgbg').style.opacity="1";

    gu1.style.top="-50px";
    gu1.style.height="140vh";
    jin1.style.top="-50px";
    jin1.style.height="140vh";
    document.getElementById('cn').style.height="105vh";
    document.getElementById('cn').style.top="-20px";
    document.getElementById('cn').style.left="-20px";

    function wenzi(){
    document.getElementById('contain').style.opacity="1";
    document.getElementById('contain').style.zIndex="9";
    }
    setTimeout(wenzi,500);
};

document.getElementById("back").onclick = function() {
    function back(){
    gu .style.left =  '0px';
    jin.style.right =  '-0px';
    gu.style.opacity="1";
    jin.style.opacity="1";
    document.getElementById('imgbg').style.opacity="0.5";

    gu1.style.top="0px";
    gu1.style.height="100vh";
    jin1.style.top="0px";
    jin1.style.height="100vh";
    document.getElementById('cn').style.height="100vh";
    document.getElementById('cn').style.top="0px";
    document.getElementById('cn').style.left="0px";
    }

    document.getElementById('contain').style.opacity="0";
    document.getElementById('contain').style.zIndex="-1";

    setTimeout(back,500);
};
