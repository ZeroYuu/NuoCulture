
document.querySelectorAll('.s_button1')[0].onclick=()=>{
    let lists=document.querySelectorAll('.y2');
    document.querySelector('#y3').appendChild(lists[0]);
}
document.querySelectorAll('.s_button')[0].onclick=()=>{
    let lists=document.querySelectorAll('.y2');
    document.querySelector('#y3').prepend(lists[lists.length-1]);

}
