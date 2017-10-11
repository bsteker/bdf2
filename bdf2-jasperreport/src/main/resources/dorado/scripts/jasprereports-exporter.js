dorado.widget.JasperreportsExporter = $extend(dorado.widget.Control, {
    $className : "dorado.widget.JasperreportsExporter",
    ATTRIBUTES : {
    	reportFile:{},
    	fileSource:{defaultValue:"uploadedFile"},
    	dataSourceType:{defaultValue:"jdbc"},
    	dataSourceProvider:{},
    	exportFileType:{defaultValue:"jrpxml"},
    	exportFileName:{defaultValue:"report"},
    	cache:{defaultValue:false},
    	parameter: {
			setter: function(parameter) {
				if (this._parameter instanceof dorado.util.Map && parameter instanceof dorado.util.Map) {
					this._parameter.put(parameter);
				} else {
					this._parameter = parameter;
				}
			}
		}    	
    },
    createDom : function() {
    	this.iframeId="_iFrame"+Math.floor(Math.random()*100);
    	this.iframe=$("<iframe id=\""+this.iframeId+"\"></iframe>");
        return this.iframe.get(0);
    },
    execute:function(){
    	var url="dorado/bdf2/jasperreports/report.exporter";
    	if(this._exportFileType && this._exportFileType=="jrpxml"){
    		url="dorado/bdf2/jasperreports/report.display?exportFileType=jrpxml";    		    		
    	}else{
    		url+="?exportFileType="+this._exportFileType;    		
    	}
    	url+="&reportFile="+this._reportFile;
    	if(this._fileSource){
    		url+="&fileSource="+this._fileSource;    		
    	}else{
    		url+="&fileSource=uploadedFile";    		    		
    	}
    	if(this._dataSourceProvider){
    		url+="&dataSourceProvider="+this._dataSourceProvider;    		
    	}
    	if(this._dataSourceType){
    		url+="&dataSourceType="+this._dataSourceType;    		    		
    	}else{
    		url+="&dataSourceType=Jdbc";    		    		    		
    	}
    	if(this._cache){
    		url+="&cache="+this._cache;
    	}
    	if(this._exportFileName){
    		url+="&exportFileName="+this._exportFileName;
    	}
    	var p=this._parameter;
		var params="";
    	if(p){
    		if(p instanceof dorado.util.Map){
    			p.eachKey(function(key,value){
    				params+=key+"====="+value+";";
    			});
    		}else if(p instanceof Array){
    			p.each(function(item){
    				for(var jsonKey in item){
    					var key=jsonKey;
    					var value=item[key];
    					params+=key+"="+value+";";
    				}
    			});
    		}
    	}
    	if(!this._width){
    		this._width="100%";
    	}
    	if(!this._height){
    		this._height="100%";
    	}
    	this.iframe.css({width:this._width,height:this._height});
		if(!this.form){
			this.form=$("<form method=\"post\" target=\""+this.iframeId+"\"></form>");
		}
		this.form.attr("action",url);
		if(params.length>1){
			this.form.append("<input type=\"hidden\" name=\"reportParameters\" value=\""+encodeURI(params)+"\"></input>");
		}
		if(this._exportFileName){
			this.form.append("<input type=\"hidden\" name=\"exportFileName\" value=\""+encodeURI(this._exportFileName)+"\"></input>");
		}
		this.form.submit();
    }
});
