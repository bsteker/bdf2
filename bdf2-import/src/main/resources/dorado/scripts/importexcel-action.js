(function() {
	dorado.widget.ImportExcelAction = $extend(dorado.widget.Action, {
		$className : "dorado.widget.ImportExcelAction",
		ATTRIBUTES : {
			async : {
				defaultValue : true
			},
			timeout : {},
			batchable : {
				defaultValue : true
			},
			excelModelId : {},
			startRow : {
				defaultValue : 0
			},
			endRow : {
				defaultVaule : 0
			},
			showImportData : {
				defaultValue : true
			},
			bigData : {
				defaultValue : false
			}
		},
		EVENTS : {

			beforeExecute : {},

			onExecute : {},

			onSuccess : {}

		},
		constructor : function() {
			this._bindingObjects = new dorado.ObjectGroup();
			$invokeSuper.call(this, arguments);
			this.addListener("onAttributeChange", function(self, arg) {
				dorado.Toolkits.setDelayedAction(self, "$actionStateChangeTimerId", function() {
					var attr = arg.attribute, value = arg.value;
					if ((attr == "icon") || (attr == "iconClass")) {
						self._bindingObjects.set(attr, value, {
							skipUnknownAttribute : true
						});
					} else {
						self._bindingObjects.invoke("onActionStateChange");
					}
				}, 20);
			});
		},

		doAddBindingObject : function(object) {
			this._bindingObjects.objects.push(object);
		},

		doRemoveBindingObject : function(object) {
			this._bindingObjects.objects.remove(object);
		},
		execute : function(callback) {
			var view = this._view, action = this, showImportData = this._showImportData, bigData = this._bigData;
			if (!action._excelModelId) {
				throw new dorado.Exception("excelModelId is null");
			}
			if (action._disabled) {
				throw new dorado.ResourceException("dorado.baseWidget.ErrorCallDisabledAction", action._id);
			}
			action.set("disabled", false);
			function doExecuteViewData() {
				uploadExcelDialog.close();
				jQuery(buttonUploadExcel.getDom()).remove();
				var buttonParseExcel = new dorado.widget.Button({
					caption : $resource("bdf2.import.ParseExcel"),
					icon : "url(>skin>common/icons.gif) -120px -280px",
					listener : {
						onClick : function(self, arg) {
							parseAction.set("parameter", action._parameter);
							parseAction.execute(function(result) {
								self.set("disabled", true);
								showCacheDialog.close();
								$callback(callback, true, result);
								action.fireEvent("onSuccess", action, result);
							});
						}
					}
				});
				var showCacheDialog = new dorado.widget.Dialog({
					width : 500,
					height : 400,
					center : true,
					closeAction : "hide",
					status : "hidden",
					maximizeable : true,
					modal : true,
					caption : $resource("bdf2.import.PreviewExcel"),
					view : view,
					buttons : [ buttonParseExcel ],
					listener : {
						onClose : function(self, arg) {
							if(parseAction){
								view.removeChild(parseAction);
							}
							if(uploadActionExcel){
								view.removeChild(uploadActionExcel);
							}
						}
					}
				});
				var url = "bdf2.importexcel.view.online.DataViewMaintain.d";
				var iframe = new dorado.widget.IFrame({
					path : url,
					height : "100%",
					width : "100%",
					path : url
				});
				showCacheDialog.addChild(iframe);
				showCacheDialog.show();
			}
			;

			function doExecuteNoViewData() {
				dorado.MessageBox.confirm($resource("bdf2.import.ConfirmImportOnUploaded"), function() {
					parseAction.set("parameter", action._parameter);
					parseAction.execute(function(result) {
						uploadExcelDialog.close();
						$callback(callback, true, result);
						action.fireEvent("onSuccess", action, result);
					});
				});
			}
			;

			var parseAction = view.id("$parseAction");
			if (parseAction == null) {
				parseAction = new dorado.widget.AjaxAction({
					view : view,
					id : "$parseAction",
					service : "bdf.ExcelMaintain#processParserdExcelData",
					executingMessage : $resource("bdf2.import.DoImportInfo")
				});
			}
			;
			var uploadActionExcel = view.id("$uploadActionExcel");
			if (uploadActionExcel == null) {
			    var fileResolver = dorado.widget.UploadAction.prototype.ATTRIBUTES.fileResolver;

				uploadActionExcel = new dorado.widget.UploadAction({
					id : "$uploadActionExcel",
					autoUpload : false,
					listener : {
						onFilesAdded : function(self, arg) {
							var s = "", name;
							if (arg.files.length > 1) {
								s = $resource("bdf2.import.ExcelFileMatchErrorInfo");
							} else {
								name = arg.files[0].name;
								if (!(new RegExp(".xls$").test(name) || new RegExp(".xlsx$").test(name))) {
									s = $resource("bdf2.import.ExcelFileMatchErrorInfo");
								}
							}
							if (s.length > 0) {
								uploadExcelDialog.close();
								throw new dorado.Exception(s);
							}
							var parameters = {
								startRow : action._startRow || 0,
								endRow : action._endRow || 0,
								excelModelId : action._excelModelId,
								bigData : action._bigData || false
							};
							dorado.Object.apply(parameters, action._parameter);
							if (self.ATTRIBUTES.multipartParams){
								self.set("multipartParams", parameters);
							} else {
								self.set("parameter", parameters);
							}
							self.taskId = dorado.util.TaskIndicator.showTaskIndicator($resource("bdf2.import.UploadingInfo") + name + "...", "main");
							self.start();
						},
						onError : function(self, arg){
							if (self.taskId) {
								dorado.util.TaskIndicator.hideTaskIndicator(self.taskId);
							}
						},
						onFileUploaded : function(self, arg) {
							if (self.taskId) {
								dorado.util.TaskIndicator.hideTaskIndicator(self.taskId);
							}
							var res = (arg.response)?arg.response.response:arg.returnValue;
							if (res && res.length > 0) {
								uploadExcelDialog.close();
								throw new dorado.Exception(res);
							}
							if (showImportData != false) {
								doExecuteViewData();
							} else {
								doExecuteNoViewData();
							}
						}
					}
				});
				
				if (fileResolver){
					uploadActionExcel.set("fileResolver", "bdf2.excelImportResolver#parseFile");
				}else{
					uploadActionExcel.set("url", "dorado/bdf2/upload.excel.do");
				}
			};
			
			var buttonUploadExcel = new dorado.widget.Button({
				caption : $resource("bdf2.import.UploadExcelFile"),
				icon : "url(>skin>common/icons.gif) -120px -280px",
				action : "$uploadActionExcel"
			});
			var uploadExcelDialog = new dorado.widget.Dialog({
				width : 350,
				height : 150,
				center : true,
				closeAction : "hide",
				status : "hidden",
				maximizeable : false,
				modal : true,
				caption : $resource("bdf2.import.ImportExcelFile"),
				view : view,
				buttons : [ buttonUploadExcel ],
				listener : {
					onClose : function(self, arg) {
						if(uploadActionExcel){
							view.removeChild(uploadActionExcel);
						}
						if(parseAction){
							view.removeChild(parseAction);
						}
					}
				}
			});
			view.addChild(uploadExcelDialog);
			var eventArg = {
				processDefault : true,
				uploadExcelDialog : uploadExcelDialog
			};
			action.fireEvent("beforeExecute", action, eventArg);
			if (eventArg.processDefault) {
				uploadExcelDialog.show();
				action.fireEvent("onExecute", action, eventArg);
			};
		}
	});
})();
