/** @Controller */

// @Bind @dataTypeUrl.#children.beforeLoadData
!function(self, arg) {
	var id=view.id("dataSetRole").getData("#.id");
	self.set("parameter",{parentId:arg.entity.get("id"),roleId:id});
}

// @Bind #dataSetRole.onLoadData
!function(self, arg) {
	if (self.getData().entityCount==0){
		view.set("^button.disabled", true);
		dorado.MessageBox.alert("请先进行系统的角色维护,有角色的前提下才能设置URL权限!");
	}
}

//@Bind #buttonSave.onClick
!function(self, arg) {
	var data=view.id("dataSetRole").getData("#");
	if(!data){
		dorado.MessageBox.alert("请先选中一个角色后再进行此操作");
		return;
	}
	var action=view.id("ajaxActionSaveRoleUrls");
	var ids=[];
	var bulidSelectedNodeId = function(urls){
		urls.each(function(url){
			if(url.get("use") == true){
				ids.push(url.get("id"));
			}
			bulidSelectedNodeId(url.get("children"));
		});
	};
	bulidSelectedNodeId(data.get("roleUrls"));

	action.set("parameter",{roleId:data.get("id"),ids:ids});
	action.execute();
}


// @Bind #dataTreeUrl.onRenderNode
!function(self, arg) {
	if(arg.node.get("data").get("use")){
		arg.dom.innerHTML="<strong><font color='green'>"+arg.label+"</font></strong>";
	}else{
		arg.dom.innerHTML=arg.label;
	}
}

//@Bind #dataTreeUrl.onNodeCheckedChange
!function(self, arg) {
	var auto = view.get("#checkBoxAutoCheckChildren.value");
	var checked = !arg.node.get("checked");
	var url = arg.node.get("data");
	var checkChildren = function(url) {
		url.get("children").each(function(child) {
			child.set("use", checked);
			checkChildren(child);
		});
	};
	if(auto) {
		checkChildren(url);
	}

}


