<%@ 		
		attribute name="top" required="true" rtexprvalue="true" type="java.lang.String" %><%@ 		
		attribute name="left" required="true" rtexprvalue="true" type="java.lang.String" %><%@ 		
		attribute name="tab1" required="true" rtexprvalue="true" type="java.lang.String" %><%@ 		
		attribute name="tab2" required="true" rtexprvalue="true" type="java.lang.String" %><%@ 		
		attribute name="tab3" required="true" rtexprvalue="true" type="java.lang.String" %><%@ 		
		attribute name="tab4" required="true" rtexprvalue="true" type="java.lang.String" %><%@ 		
		attribute name="bottom" required="true" rtexprvalue="true" type="java.lang.String" %>
		<div class="oas_feature_frame" style="padding-top: 10px;">
			<span>
				<script type="text/javascript">
					OAS_AD('<%= top %>');
					showExtCampaignButtons();
				</script>
			</span>
		</div>			
		<div class="oas_feature_left left">
			<script type="text/javascript">
				OAS_AD('<%= left %>');
			</script>
   		</div>
   		<div class="oas_feature_right right">
   			<div class="oas_feature_right_tab">
	   			<script type="text/javascript">
					OAS_AD('<%= tab1 %>');
				</script>
			</div>
   			<div class="oas_feature_right_tab">
	   			<script type="text/javascript">
					OAS_AD('<%= tab2 %>');
				</script>
			</div>
   			<div class="oas_feature_right_tab">
	   			<script type="text/javascript">
					OAS_AD('<%= tab3 %>');
				</script>
			</div>
   			<div class="oas_feature_right_tab">
	   			<script type="text/javascript">
					OAS_AD('<%= tab4 %>');
				</script>
			</div>
   		</div>
	   	<div class="clear" style="font-size: 0px;"></div>
	   	
		<div class="oas_feature_frame">
			<span>
	   			<script type="text/javascript">
					OAS_AD('<%= bottom %>');
				</script>
			</span>
		</div>
