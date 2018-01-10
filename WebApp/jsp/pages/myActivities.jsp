<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>           
<style>
/* Panel - Activity Stream
---------------------------*/
.activity-stream ul.list-stream,
.panel-comment ul.list-stream,
.activity-stream ul.list-comment,
.panel-comment ul.list-comment {
  margin-top: -15px;
  margin-bottom: -15px;
}
.activity-stream ul.list-stream .list-stream-item,
.panel-comment ul.list-stream .list-stream-item,
.activity-stream ul.list-comment .list-stream-item,
.panel-comment ul.list-comment .list-stream-item {
  border-left: 1px solid #e1e3e4;
  padding-top: 18px;
  padding-bottom: 18px;
  padding-right: 15px;
  margin-left: 15px;
  margin-right: -15px;
  border-top: 1px solid #e1e3e4;
}
.activity-stream ul.list-stream .list-stream-item:first-child,
.panel-comment ul.list-stream .list-stream-item:first-child,
.activity-stream ul.list-comment .list-stream-item:first-child,
.panel-comment ul.list-comment .list-stream-item:first-child {
  border-top: 0;
}
.activity-stream ul.list-stream .list-stream-item.minified .media-body,
.panel-comment ul.list-stream .list-stream-item.minified .media-body,
.activity-stream ul.list-comment .list-stream-item.minified .media-body,
.panel-comment ul.list-comment .list-stream-item.minified .media-body {
  display: inline-block;
}
.activity-stream ul.list-stream .list-stream-item.minified p,
.panel-comment ul.list-stream .list-stream-item.minified p,
.activity-stream ul.list-comment .list-stream-item.minified p,
.panel-comment ul.list-comment .list-stream-item.minified p {
  width: 80%;
  float: left;
}
.activity-stream ul.list-stream .list-stream-item.load .list-stream-icon,
.panel-comment ul.list-stream .list-stream-item.load .list-stream-icon,
.activity-stream ul.list-comment .list-stream-item.load .list-stream-icon,
.panel-comment ul.list-comment .list-stream-item.load .list-stream-icon {
  margin-top: -5px;
}
.activity-stream ul.list-stream .list-stream-icon,
.panel-comment ul.list-stream .list-stream-icon,
.activity-stream ul.list-comment .list-stream-icon,
.panel-comment ul.list-comment .list-stream-icon {
  margin-left: -13px;
  -webkit-border-radius: 50px;
  -moz-border-radius: 50px;
  border-radius: 50px;
  background: #7f8c8d;
  color: #ffffff;
  width: 25px;
  height: 25px;
  text-align: center;
}
.activity-stream ul.list-stream .list-stream-icon i,
.panel-comment ul.list-stream .list-stream-icon i,
.activity-stream ul.list-comment .list-stream-icon i,
.panel-comment ul.list-comment .list-stream-icon i {
  line-height: 25px;
  font-size: 13px;
}
.activity-stream .media,
.panel-comment .media {
  margin: 0 !important;
  padding-left: 20px;
  font-family: 'Karla', sans-serif;
}
.activity-stream .media .media-heading,
.panel-comment .media .media-heading {
  margin: 0 !important;
  font-size: 14px;
  font-weight: 400;
  padding: 5px 0;
  font-family: 'Montserrat', sans-serif;
}
.activity-stream .media .media-heading a,
.panel-comment .media .media-heading a {
  color: #e74c3c;
}
.activity-stream .media .media-heading a:hover,
.panel-comment .media .media-heading a:hover {
  color: #d62c1a;
}
.activity-stream .media .media-body,
.panel-comment .media .media-body {
  width: 100%;
  margin: 0 !important;
}
.activity-stream .media .meta-time,
.panel-comment .media .meta-time {
  font-weight: 400;
  font-family: 'Montserrat', sans-serif;
  color: rgba(68, 76, 82, 0.6);
  font-size: 12px;
  margin-bottom: 10px;
  position: absolute;
  right: 20px;
}
.activity-stream .media .media-avatar,
.panel-comment .media .media-avatar {
  overflow: hidden;
  text-align: center;
  border-radius: 3px;
  background: #ffffff;
  margin-right: 15px;
}
.activity-stream .media .media-avatar img,
.panel-comment .media .media-avatar img {
  width: 60px;
  -webkit-border-radius: 3px;
  -moz-border-radius: 3px;
  border-radius: 3px;
}
.activity-stream .media .media-avatar.mini img,
.panel-comment .media .media-avatar.mini img {
  width: 23px;
  height: 23px;
}
.activity-stream .media p,
.panel-comment .media p {
  margin-bottom: 0;
  line-height: 20px;
}
.activity-stream .media .grouped-avatar,
.panel-comment .media .grouped-avatar {
  margin-top: 10px;
}
.activity-stream .media .grouped-avatar .media-avatar,
.panel-comment .media .grouped-avatar .media-avatar {
  width: 20px;
  height: 20px;
  margin-right: 5px;
}
.activity-stream .media .loadmore,
.panel-comment .media .loadmore {
  position: relative;
  top: 1px;
  font-family: 'Montserrat', sans-serif;
  text-transform: uppercase;
  font-size: 11px;
}

.color-primary {
  color: #2980b9 !important;
}
.color-success {
  color: #27ae60 !important;
}
.color-info {
  color: #3498db !important;
}
.color-warning {
  color: #f39c12 !important;
}
.color-danger {
  color: #c0392b !important;
}
.bg-primary {
  background: #2980b9 !important;
}
.bg-success {
  background: #27ae60 !important;
}
.bg-info {
  background: #3498db !important;
}
.bg-warning {
  background: #f39c12 !important;
}
.bg-danger {
  background: #c0392b !important;
}
</style>

 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">My Activities</li>
 </ul>
 <!-- END BREADCRUMB -->  

<!-- PAGE TITLE -->
<div class="page-title">                    
    <h2><span class="fa fa-users"></span> My Task Activities</h2>
</div>
<!-- END PAGE TITLE -->                

<!-- PAGE CONTENT WRAPPER -->
<div class="page-content-wrap">
    
    <div class="row">
        <div class="col-md-12">
				<div class="panel panel-default panel-stream">
					<div class="panel-body">
						<div class="activity-stream">
							<ul class="list-unstyled list-stream">
								<li class="list-stream-item">
									<div class="list-stream-icon pull-left bg-primary">
										<i class="fa fa-tint"></i>
									</div>
									<div class="media">
										<a class="media-avatar mini media-left" href="#">
											<img class="media-object" src="img/uif-1.jpg" alt="...">
										</a>
										<div class="media-body">
											<h4 class="media-heading">
												<a href="#">John Appleseed</a>
												<span class="meta-time">2 hours ago</span>
											</h4>
											<p>
												Created a new project: <a href="#">This is Awesome Project</a>
											</p>
										</div>
									</div>
								</li>
								<li class="list-stream-item">
									<div class="list-stream-icon pull-left bg-info">
										<i class="fa fa-comments"></i>
									</div>
									<div class="media">
										<a class="media-avatar mini media-left" href="#">
											<img class="media-object" src="img/uif-2.jpg" alt="...">
										</a>
										<div class="media-body">
											<h4 class="media-heading">
												<a href="#">Steve Woz</a>
												<span class="meta-time">20 hours ago</span>
											</h4>
											<p>
												Commented on: <a href="#">This is Awesome Project</a>
											</p>
										</div>
									</div>
								</li>
								<li class="list-stream-item">
									<div class="list-stream-icon pull-left bg-success">
										<i class="fa fa-check-square-o"></i>
									</div>
									<div class="media">
										<a class="media-avatar mini media-left" href="#">
											<img class="media-object" src="img/uif-3.jpg" alt="...">
										</a>
										<div class="media-body">
											<h4 class="media-heading">
												<a href="#">Chris Mullin</a>
												<span class="meta-time">Yesterday</span>
											</h4>
											<p>
												Completed a task in: <a href="#">Milestone #20</a>
											</p>
										</div>
									</div>
								</li>
								<li class="list-stream-item">
									<div class="list-stream-icon pull-left bg-primary">
										<i class="fa fa-tint"></i>
									</div>
									<div class="media">
										<a class="media-avatar mini media-left" href="#">
											<img class="media-object" src="img/uif-1.jpg" alt="...">
										</a>
										<div class="media-body">
											<h4 class="media-heading">
												<a href="#">John Appleseed</a>
												<span class="meta-time">2 hours ago</span>
											</h4>
											<p>
												Created a new Project: <a href="#">This is Awesome Project</a>
											</p>
										</div>
									</div>
								</li>
								<li class="list-stream-item minified">
									<div class="list-stream-icon pull-left bg-info">
										<i class="fa fa-comments"></i>
									</div>
									<div class="media">
										<div class="media-body">
											<p>
												2 Comments made on Task: <a href="#">This is Awesome Project</a>
											</p>
											<span class="meta-time">2 hours ago</span>
										</div>
										<div class="grouped-avatar clearfix">
											<a class="media-avatar mini pull-left" href="#" data-toggle="tooltip" data-placement="top" title="" data-original-title="John Appleseed">
												<img class="media-object" src="img/uif-1.jpg" alt="...">
											</a>
											<a class="media-avatar mini pull-left" href="#" data-toggle="tooltip" data-placement="top" title="" data-original-title="Kung Fu Panda">
												<img class="media-object" src="img/uif-7.jpg" alt="...">
											</a>
										</div>
									</div>
								</li>
								<li class="list-stream-item">
									<div class="list-stream-icon pull-left bg-success">
										<i class="fa fa-check-square-o"></i>
									</div>
									<div class="media">
										<a class="media-avatar mini media-left" href="#">
											<img class="media-object" src="img/uif-3.jpg" alt="...">
										</a>
										<div class="media-body">
											<h4 class="media-heading">
												<a href="#">Chris Mullin</a>
												<span class="meta-time">Yesterday</span>
											</h4>
											<p>
												Completed a Task in: <a href="#">Milestone #20</a>
											</p>
										</div>
									</div>
								</li>
								<li class="list-stream-item minified">
									<div class="list-stream-icon pull-left bg-info">
										<i class="fa fa-comment"></i>
									</div>
									<div class="media">
										<div class="media-body">
											<p class="pull-left">
												2 Comments made on Task: <a href="#">This is Awesome Project</a>
											</p>
											<span class="meta-time">2 hours ago</span>
										</div>
										<div class="grouped-avatar clearfix">
											<a class="media-avatar mini pull-left" href="#" data-toggle="tooltip" data-placement="top" title="" data-original-title="John Appleseed">
												<img class="media-object" src="img/uif-1.jpg" alt="...">
											</a>
											<a class="media-avatar mini pull-left" href="#" data-toggle="tooltip" data-placement="top" title="" data-original-title="Kung Fu Panda">
												<img class="media-object" src="img/uif-7.jpg" alt="...">
											</a>
										</div>
									</div>
								</li>
								<li class="list-stream-item">
									<div class="list-stream-icon pull-left bg-success">
										<i class="fa fa-check-square-o"></i>
									</div>
									<div class="media">
										<a class="media-avatar mini media-left" href="#">
											<img class="media-object" src="img/uif-3.jpg" alt="...">
										</a>
										<div class="media-body">
											<h4 class="media-heading">
												<a href="#">Chris Mullin</a>
												<span class="meta-time">Yesterday</span>
											</h4>
											<p>
												Completed a Task in: <a href="#">Milestone #20</a>
											</p>
										</div>
									</div>
								</li>
								<li class="list-stream-item minified load">
									<div class="list-stream-icon pull-left">
										<i class="fa fa-repeat"></i>
									</div>
									<div class="media">
										<div class="media-body">
											<a href="#" class="loadmore">Load More</a>
										</div>
									</div>
								</li>
							</ul>
						</div>
					</div>
				</div>
        </div>
    </div>
</div>