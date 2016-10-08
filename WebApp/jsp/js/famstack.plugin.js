 var templatePlugins = function(){
        
        var tp_clock = function(hour, minutes){
            
            function tp_clock_time(hour, minutes){
            	console.log(hour);
                hour = hour < 10 ? '0'+hour : hour;
                minutes = minutes < 10 ? '0'+minutes : minutes;
                
                $(".plugin-clock").html(hour+"<span>:</span>"+minutes);
            }
            if($(".plugin-clock").length > 0){
                
                tp_clock_time(hour, minutes);
                
                window.setInterval(function(){
                    tp_clock_time(hour, minutes);                    
                },10000);
            }
        }
        
        var tp_date = function(){
            
            if($(".plugin-date").length > 0){
                
                var days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
                var months = ['January','February','March','April','May','June','July','August','September','October','November','December'];
                        
                var now     = new Date();
                var day     = days[now.getDay()];
                var date    = now.getDate();
                var month   = months[now.getMonth()];
                var year    = now.getFullYear();
                
                $(".plugin-date").html(day+", "+month+" "+date+", "+year);
            }
            
        }
        
        return {
            init: function(hour, minutes){
            	console.log(hour);
                tp_clock(hour, minutes);
                tp_date();
            }
        }
    }();

    $(document).ready(function(){        
        
        /* PROGGRESS START */
        $.mpb("show",{value: [0,50],speed: 5,state: 'danger'});      
        /* END PROGGRESS START */
        
        
        /* PROGGRESS COMPLETE */
        $.mpb("update",{value: 100, speed: 5, complete: function(){            
            $(".mpb").fadeOut(200,function(){
                $(this).remove();
            });
        }});
        /* END PROGGRESS COMPLETE */
    });
    
    /* MESSAGE BOX */
    $(".mb-control").on("click",function(){
        var box = $($(this).data("box"));
        if(box.length > 0){
            box.toggleClass("open");
            
            var sound = box.data("sound");
            
            if(sound === 'alert')
                playAudio('alert');
            
            if(sound === 'fail')
                playAudio('fail');
            
        }        
        return false;
    });
    $(".mb-control-close").on("click",function(){
       $(this).parents(".message-box").removeClass("open");
       return false;
    });    
    /* END MESSAGE BOX */
 