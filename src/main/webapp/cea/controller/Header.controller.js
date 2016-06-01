sap.ui.define([ 'jquery.sap.global', 'sap/ui/core/mvc/Controller', 'sap/m/Popover', 'sap/m/Button', 
                'sap/ui/model/json/JSONModel', 'sap/m/PlacementType', 'sap/m/ButtonType' ], 
	function(jQuery, Controller, Popover, Button, JSONModel, PlacementType, ButtonType) {
	"use strict";
	var Controller = Controller.extend("concur.expenses.analyzer.controller.Header", {
		onInit : function() {
			var oUserModel = new JSONModel();
			oUserModel.loadData("/rest/user");
			this.getView().setModel(oUserModel, "user");
		},
		onUserNamePress : function(event) {
			var popover = new Popover({
				showHeader : false,
				placement : PlacementType.Bottom,
				content : [ new Button({
					text : 'Logout',
					type : ButtonType.Transparent,
					press: function(){
						jQuery.ajax({
							type : "GET",
							url : "./rest/logout",
							async: false, 
							success : function() {
								window.location.reload();
							}
						});							
					}
				})]
			}).addStyleClass('sapMOTAPopover sapTntToolHeaderPopover');

			popover.openBy(event.getSource());
		}
	});

	return Controller;

});