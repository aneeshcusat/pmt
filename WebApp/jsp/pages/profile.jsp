<%@include file="includes/header.jsp" %>
<style>
*{
    font-family: 'Open Sans', sans-serif;
}

.well {
    margin-top:-20px;
    background-color:#007FBD;
    border:2px solid #0077B2;
    text-align:center;
    cursor:pointer;
    font-size: 25px;
    padding: 15px;
    border-radius: 0px !important;
}

.well:hover {
    margin-top:-20px;
    background-color:#0077B2;
    border:2px solid #0077B2;
    text-align:center;
    cursor:pointer;
    font-size: 25px;
    padding: 15px;
    border-radius: 0px !important;
    border-bottom : 2px solid rgba(97, 203, 255, 0.65);
}

body {
font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
font-size: 14px;
line-height: 1.42857143;
color: #fff;
background-color: #F1F1F1;
}

.bg_blur
{
    background-image:url('http://data2.whicdn.com/images/139218968/large.jpg');
    height: 300px;
    background-size: cover;
}

.follow_btn {
    text-decoration: none;
    position: absolute;
    left: 35%;
    top: 42.5%;
    width: 35%;
    height: 15%;
    background-color: #007FBE;
    padding: 10px;
    padding-top: 6px;
    color: #fff;
    text-align: center;
    font-size: 20px;
    border: 4px solid #007FBE;
}

.follow_btn:hover {
    text-decoration: none;
    position: absolute;
    left: 35%;
    top: 42.5%;
    width: 35%;
    height: 15%;
    background-color: #007FBE;
    padding: 10px;
    padding-top: 6px;
    color: #fff;
    text-align: center;
    font-size: 20px;
    border: 4px solid rgba(255, 255, 255, 0.8);
}

.header{
    color : #808080;
    margin-left:12%;
    margin-top:23px;
}

.picture{
    height:150px;
    width:150px;
    position:absolute;
    top: 41px;
    left:-70px;
}

.picture_mob{
    position: absolute;
    width: 35%;
    left: 35%;
    bottom: 70%;
}

.btn-style{
    color: #fff;
    background-color: #007FBE;
    border-color: #adadad;
    width: 33.3%;
}

.btn-style:hover {
    color: #333;
    background-color: #3D5DE0;
    border-color: #adadad;
    width: 33.3%;
   
}


@media (max-width: 767px) {
    .header{
        text-align : center;
    }
    
    
    
    .nav{
        margin-top : 30px;
    }
}

</style>
  <div class="content-frame">                                    
       <!-- START CONTENT FRAME TOP -->
       <div class="content-frame-top">                        
           <div class="page-title">                    
               <h2><span class="fa fa-comments"></span> Profile</h2>
           </div>                                                    
       </div>
   <div class="container" style="margin-top: 20px; margin-bottom: 20px;">
	<div class="row panel">

        <div class="col-md-8  col-xs-12">
           <img src="${applicationHome}/image/${userProile.id}" class="img-thumbnail picture hidden-xs" />
           <img src="http://lorempixel.com/output/people-q-c-100-100-1.jpg" class="img-thumbnail visible-xs picture_mob" />
           <div class="header">
                <h2>${userProile.firstName} ${userProile.lastName}</h2>
                <h5><p>Designation 	: <b>${userProile.designation}</b></p></h5>
                <h5><p>Gender 		: <b>${userProile.gender}</b></p></h5>
                <h5><p>Mob No 		: <b>${userProile.mobileNumber}</b></p></h5>
                <h5><p>Email 		: <b>${userProile.userId}</b></p></h5>
                <h5><p>Manager 		: <b>${userProile.reportertingManager.firstName}</b></p></h5>
           </div>
        </div>
    </div>   
    
	<div class="row nav">    
        <div class="col-md-1"></div>
        <div class="col-md-11 col-xs-12" style="margin: 0px;padding: 0px;">
            <div class="col-md-4 col-xs-4 well"><i class="fa fa-star fa-lg"></i> 0</div>
            <div class="col-md-4 col-xs-4 well"><i class="fa fa-heart-o fa-lg"></i> 0</div>
            <div class="col-md-4 col-xs-4 well"><i class="fa fa-thumbs-o-up fa-lg"></i> 0</div>
        </div>
    </div>
</div>
</div>
<%@include file="includes/footer.jsp" %>     