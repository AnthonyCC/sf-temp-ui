<%@
		attribute name="main" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="left1" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="right1" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="left2" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="right2" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="left3" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="right3" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="mainTopBar" required="false" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="full" required="false" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="fullTopBar" required="false" rtexprvalue="true" type="java.lang.String" %>

    <div class="oas-homepageredesign">
      <div class="full-top-bar full">
        <script type="text/javascript">
          OAS_AD('<%= fullTopBar %>');
        </script>
      </div>
      <div class="full-carousel full">
        <script type="text/javascript">
        OAS_AD('<%= full %>');
        </script>
      </div>
      <div class="main-top-bar full">
        <script type="text/javascript">
          OAS_AD('<%= mainTopBar %>');
        </script>
      </div>
      <div class="oas-container full">
        <div class="main half">
          <script type="text/javascript">
          OAS_AD('<%= main %>');
          </script>
        </div>
        <div class="right-side half">
          <div class="line">
            <div class="half">
              <script type="text/javascript">
              OAS_AD('<%= left1 %>');
              </script>
            </div>
            <div class="half right">
              <script type="text/javascript">
              OAS_AD('<%= right1 %>');
              </script>
            </div>
          </div>
          <div class="line">
            <div class="half">
              <script type="text/javascript">
              OAS_AD('<%= left2 %>');
              </script>
            </div>
            <div class="half right">
              <script type="text/javascript">
              OAS_AD('<%= right2 %>');
              </script>
            </div>
          </div>
          <div class="line">
            <div class="half">
              <script type="text/javascript">
              OAS_AD('<%= left3 %>');
              </script>
            </div>
            <div class="half right">
              <script type="text/javascript">
              OAS_AD('<%= right3 %>');
              </script>
            </div>
          </div>
        </div>
      </div>
    </div>
