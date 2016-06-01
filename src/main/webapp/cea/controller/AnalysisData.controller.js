sap.ui.define([ "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/viz/ui5/format/ChartFormatter", 
                "sap/ui/core/format/NumberFormat" ],
		function(Controller, JSONModel, ChartFormatter, NumberFormat) {
		"use strict";
		return Controller.extend("concur.expenses.analyzer.controller.AnalysisData",{
			onInit : function() {
				var dateToday = new Date();
				dateToday.setHours(0, 0, 0, 0);
				var dateLastMonth = new Date(dateToday.getTime());
				dateLastMonth.setMonth(dateLastMonth.getMonth() - 1);

				this.initDateModel(dateToday,dateLastMonth);
				this.initExpensesAnalysesModel();

				this.initExpensesAnalysesVizFrame();
				this.updateExpensesAnalysesModel(
						dateLastMonth.getTime(),
						dateToday.getTime());
			},
			initDateModel : function(dateToday, dateLastMonth) {
				var oDateModel = new JSONModel();
				oDateModel.setData({
					delimiter : " / ",
					dateFormat : "yyyy/MM/dd",
					dateValue1 : dateLastMonth,
					dateValue2 : dateToday,
				});
				this.getView().setModel(oDateModel, "date");
			},
			initExpensesAnalysesModel : function() {
				var oExpensesAnalysesModel = new JSONModel();
				this.getView().setModel(oExpensesAnalysesModel, "oExpensesAnalysesModel");
			},
			initExpensesAnalysesVizFrame : function() {
				var oVizFrame = this.getView().byId("expensesAnalysesVizFrame");
				oVizFrame.setModel(this.getView().getModel("oExpensesAnalysesModel"));

				var FIORI_PERCENTAGE_FORMAT_2 = "__UI5__PercentageMaxFraction2";
				var chartFormatter = ChartFormatter.getInstance();
				chartFormatter.registerCustomFormatter(FIORI_PERCENTAGE_FORMAT_2,
						function(value) {
							var percentage = NumberFormat.getPercentInstance({
								style : 'precent',
								maxFractionDigits : 2
							});
							return percentage.format(value);
				});

				sap.viz.api.env.Format.numericFormatter(chartFormatter);
				oVizFrame.setVizProperties({
					plotArea : {
						showGap : true,
						mode : "percentage",
						dataLabel : {
							type : "percentage",
							formatString : FIORI_PERCENTAGE_FORMAT_2,
							visible : true
						}
					},
					valueAxis : {
						title : {
							visible : false
						}
					},
					categoryAxis : {
						title : {
							visible : false
						}
					},
					title : {
						visible : false
					}
				});
			},
			updateExpensesAnalysesModel : function(dateFrom, dateTo) {
				var today = new Date();
				var userOffsetFromGmt = today.getTimezoneOffset() / 60; 
				dateFrom = dateFrom - userOffsetFromGmt*3600000;
				dateTo = dateTo - userOffsetFromGmt*3600000;
				
				var oVizFramePath = "/rest/expenses/analyses?fromDate=" + dateFrom
													+ "&toDate=" + dateTo;
				this.getView().getModel("oExpensesAnalysesModel").loadData(oVizFramePath);
			},
			handleDateChange : function(oEvent) {
				var dateFrom = Date.parse(oEvent.getParameter("from"));
				var dateTo = Date.parse(oEvent.getParameter("to"));
				
				this.updateExpensesAnalysesModel(dateFrom, dateTo);
			}
		});
});