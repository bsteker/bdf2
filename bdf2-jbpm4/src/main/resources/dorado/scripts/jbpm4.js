dorado.widget.Jbpm4ProcessImage = $extend(dorado.widget.Control, {
    $className : "dorado.widget.Jbpm4ProcessImage",
    ATTRIBUTES : {
        executionId:{
        },
		taskId:{
		}
    },
    createDom : function() {
    	var url="dorado/bdf2/jbpm4/inprogress.processimage";
    	if(this._executionId){
    		url+="?executionId="+this._executionId;
    	}else if(this._taskId){
    		url+="?taskId="+this._taskId;    		
    	}
    	var iframe=$("<iframe style=\"width:100%;height:100%\" src=\""+url+"\">");
        return iframe.get(0);
    },
    refreshDom: function(dom) {
    	$invokeSuper.call(this, arguments);
    	var url="dorado/bdf2/jbpm4/inprogress.processimage";
    	if(this._executionId){
    		url+="?executionId="+this._executionId;
    	}else if(this._taskId){
    		url+="?taskId="+this._taskId;    		
    	}
    	$(dom).attr("src",url);
    }  
});
