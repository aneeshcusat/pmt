var templatePlugins = function(){
        
        var tp_clock = function(hour, minutes){
            
            function tp_clock_time(hour, minutes){
            	
            	hour = new Date().getHours();
            	minutes =new Date().getMinutes();
            	
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
    
    /* MESSAGES LOADING */
    $(".messages .item").each(function(index){
        var elm = $(this);
        setInterval(function(){
            elm.addClass("item-visible");
        },index*300);              
    });
    /* END MESSAGES LOADING */  

    

    var showNotification = function(titile, message, url){
    	$.notification({
    		// options
    		icon: 'fa fa-exclamation-triangle fa-3x col-xs-1',
    		title: titile,
    		message: message,
    		url: url,
    		target: '_blank'
    	},{
    		// settings
    		element: 'body',
    		position: null,
    		type: "minimalist",
    		allow_dismiss: true,
    		newest_on_top: false,
    		showProgressbar: false,
    		placement: {
    			from: "top",
    			align: "right"
    		},
    		offset: 20,
    		spacing: 10,
    		z_index: 1031,
    		delay: 5000,
    		timer: 1000,
    		url_target: '_blank',
    		mouse_over: null,
    		animate: {
    			enter: 'animated fadeInDown',
    			exit: 'animated fadeOutUp'
    		},
    		onShow: null,
    		onShown: null,
    		onClose: null,
    		onClosed: null,
    		icon_type: 'class',
    		template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0}" role="alert">' +
    			'<button type="button" aria-hidden="true" class="close" data-notify="dismiss">×</button>' +
    			'<span data-notify="icon" style="color:blue"></span> ' +
    			'<span class="col-xs-10">' +
    				'<span data-notify="title" class="col-xs-12 alert-minimalist-title">{1}</span>' +
    				'<span data-notify="message"  class="col-xs-12 alert-minimalist-message">{2}</span>' +
    				'<a href="{3}" target="{4}" data-notify="url"></a>' +
    			'</span>' +
    		'</div>'
    	});
    }
    
    

    var sortSelect = function (select, attr, order) {
    	var preSelectedIndex =  $(select).get(0).selectedIndex;
        if(attr === 'text'){
            if(order === 'asc'){
                $(select).html($(select).children('option').sort(function (x, y) {
                	if ($(x).val() == '0' || $(x).val() == '-1') {
                		return -1;
                	}
                    return $(x).text().toUpperCase() < $(y).text().toUpperCase() ? -1 : 1;
                }));
                if (preSelectedIndex == 0) {
                	$(select).get(0).selectedIndex = 0;
                }
            }// end asc
            if(order === 'desc'){
                $(select).html($(select).children('option').sort(function (y, x) {
                    return $(x).text().toUpperCase() < $(y).text().toUpperCase() ? -1 : 1;
                }));
                $(select).get(0).selectedIndex = 0;
            }// end desc
        }

    };

    function roundToTwo(num) {    
        return +(Math.round(num + "e+2")  + "e-2");
    }