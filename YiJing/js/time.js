var rotateVal = 0 // 旋转角度
var sg = document.querySelectorAll("p")
			var InterVal // 定时器
			window.onload = function () {
				// 网页加载完成后即运行rotate函数
				rotate()
				// 鼠标悬浮在图片上时，停止旋转，即清除定时器
				document.getElementById('taiji').onmousemove = function () {
					clearInterval(InterVal);
					document.getElementById('tj').style.opacity=0.5
					for(var i = 0; i < sg.length; i++){
						sg[i].style.transition = "1s";
						sg[i].style.opacity=1;
						document.getElementById('introduce').style.opacity=0.3;
					}
					
				}
				// 鼠标离开图片时，继续旋转，即继续运行定时器
				document.getElementById('taiji').onmouseleave = function () {
					rotate();
					document.getElementById('tj').style.opacity=1
					for(var i = 0; i < sg.length; i++){
						sg[i].style.opacity=0;
						document.getElementById('introduce').style.opacity=1;
					}
					
				}
			}
			
			// 设置定时器
			function rotate () {
				InterVal = setInterval(function () {
					var taiji = document.getElementById('taiji');
                    taiji.style.transformOrigin='380px 360px';
					rotateVal += 0.3
					// 设置旋转属性(顺时针)
					taiji.style.transform = 'rotate(' + rotateVal + 'deg)'
					for(var i = 0; i < sg.length; i++){
						sg[i].style.transition = "0s";
						sg[i].style.transform = 'rotate(-' + rotateVal + 'deg)'
					}
					// 设置旋转属性(逆时针)
					//taiji.style.transform = 'rotate(-' + rotateVal + 'deg)'
					// 设置旋转时的动画  匀速0.1s
					taiji.style.transition = '0.1s linear'
				}, 100)
			}