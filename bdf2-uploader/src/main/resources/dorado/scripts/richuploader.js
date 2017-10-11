dorado.widget.RichUploader = $extend(dorado.widget.Control, {
    $className : "dorado.widget.RichUploader",
    ATTRIBUTES : {
        processor:{
        },
        caption:{
        },
        allowFileTypes:{
        },
        allowMaxFileSize:{
        },
        disabled:{defaultValue:false},
        autoSubmit:{defaultValue:true},
        width:{defaultValue:60},
        height:{defaultValue:26}
    },
    createDom : function() {
    	var url="dorado/bdf2/uploader/process.upload";
    	var targetProcessor="";
    	if(this._processor){
    		targetProcessor=this._processor;
    	}
    	var uploadAllowFileTypes="";
    	if(this._allowFileTypes){
    		uploadAllowFileTypes=this._allowFileTypes;
    	}
    	var uploadAllowMaxFileSize="";
    	if(this._allowMaxFileSize){
    		uploadAllowMaxFileSize=this._allowMaxFileSize;
    	}
    	var button=$("<input type=\"button\" name='button_upload'>");
    	button.css("width",this._width);
    	button.css("height",this._height);
    	if(this._style){
    		button.css(eval("({"+this._style+"})"));
    	}
    	this.uploadButton=button;
    	
    	if(this._caption){
    		button.attr("value",this._caption);
    	}
    	var self=this;
    	this.uploader=$(button).upload({
            name:"targetFile",
            action:url,
            enctype: 'multipart/form-data',
            params: {_uploadProcessor:targetProcessor,_allowFileTypes:uploadAllowFileTypes,_allowMaxFileSize:uploadAllowMaxFileSize},
            onSubmit: function() {
            	self.fireEvent("beforeSubmit",self,{});
            },
            onComplete: function(response) {
            	var res=eval("("+response+")");
            	var result=res.result;
            	if(result && result=="success"){
            		self.fireEvent("onSuccess",self,{filename:res.filename,id:res.id});
            	}else{
            		self.fireEvent("onFail",self,{errorMessage:res.errorMessage});            		
            	}
            },
            onSelect: function() {
            	self.fireEvent("onSelect",self,{filename:self.uploader.filename()});
            }
    	});
    	this.uploader.autoSubmit = this._autoSubmit;
    	if(this._disabled==true){
    		this.uploader.inputFile.attr("disabled",true);
    		this.uploadButton.attr("disabled",true);
    	}
    	if(this._visible==true){
    		if(this._hideMode=="visibility"){
    			this.uploader.inputFile.css("visibility","visible");
    			this.uploadButton.css("visibility","visible");    			
    		}else{
    			this.uploader.inputFile.css("display","");
    			this.uploadButton.css("display","");    			
    		}
    	}else{
    		if(this._hideMode=="visibility"){
    			this.uploader.inputFile.css("visibility","hidden");
    			this.uploadButton.css("visibility","hidden");    			
    		}else{
    			this.uploader.inputFile.css("display","none");
    			this.uploadButton.css("display","none");    			
    		}
    	}
        return this.uploader.uploadContainer[0];
    },
    setVisible:function(value){
    	if(value==true){
    		if(this._hideMode=="visibility"){
    			this.uploader.inputFile.css("visibility","visible");
    			this.uploadButton.css("visibility","visible");    			
    		}else{
    			this.uploader.inputFile.css("display","");
    			this.uploadButton.css("display","");    			
    		}
    	}else{
    		if(this._hideMode=="visibility"){
    			this.uploader.inputFile.css("visibility","hidden");
    			this.uploadButton.css("visibility","hidden");    			
    		}else{
    			this.uploader.inputFile.css("display","none");
    			this.uploadButton.css("display","none");    			
    		}
    	}
    },
    setDisabled:function(value){
    	if(value){
    		this.uploader.inputFile.attr("disabled",true);
    		this.uploadButton.attr("disabled",true);
    	}else{
    		this.uploader.inputFile.attr("disabled",false);
    		this.uploadButton.attr("disabled",false);
    		
    	}
    },
    refreshDom: function(dom) {
    	$invokeSuper.call(this, arguments);
    	var targetProcessor="";
    	if(this._processor){
    		targetProcessor=this._processor;
    	}
    	var uploadAllowFileTypes="";
    	if(this._allowFileTypes){
    		uploadAllowFileTypes=this._allowFileTypes;
    	}
    	var uploadAllowMaxFileSize="";
    	if(this._allowMaxFileSize){
    		uploadAllowMaxFileSize=this._allowMaxFileSize;
    	}
    	this.uploader.params({_uploadProcessor:targetProcessor,_allowFileTypes:uploadAllowFileTypes,_allowMaxFileSize:uploadAllowMaxFileSize});
    	var button=$(dom);
    	button.attr("value",this._caption);
    	button.css("width",this._width);
    	button.css("height",this._height);
    	if(this._showBorder){
    		button.css("border","none");    		
    	}
    },
    setParameter:function(parameters){
    	if(!parameters){
    		return;
    	}
    	parameters["_uploadProcessor"]=this._processor;
    	parameters["_allowFileTypes"]=this._allowFileTypes;
    	parameters["_allowMaxFileSize"]=this._allowMaxFileSize;
    	this.uploader.set({params:parameters});
    },
    submit:function(){
    	this.uploader.submit();
    },
    getFilename:function(){
    	this.uploader.filename();
    },
    EVENTS:{
    	onSelect:{},
    	beforeSubmit:{},
    	onSuccess:{},
    	onFail:{}
    }
});
