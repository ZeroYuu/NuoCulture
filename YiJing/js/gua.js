document.querySelectorAll('.s_button1')[0].onclick=()=>{
    let lists=document.querySelectorAll('.one');
    let lists1=document.querySelectorAll('.i1');
    document.querySelector('#g1').appendChild(lists[0]);
    document.querySelector('#g2').appendChild(lists1[0]);
}
document.querySelectorAll('.s_button')[0].onclick=()=>{
    let lists=document.querySelectorAll('.one');
    let lists1=document.querySelectorAll('.i1');
    document.querySelector('#g1').prepend(lists[lists.length-1]);
    document.querySelector('#g2').prepend(lists1[lists1.length-1]);
}
