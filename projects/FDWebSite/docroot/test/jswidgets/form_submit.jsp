<%@page language="java" contentType="application/json;charset=UTF-8" %>
{
  "validationResult": {
    "fdform": "form-example-2",
    "errors": [
      {
        "name": "invalidfield",
        "error": "AJAX validation error here, by submit handler"
      }
    ]
  },
  "submitForm": {
    "fdform": "form-example-2",
    "success": true,
    "result": {
      "fillForm": {
        "fdform": "form-example-2",
        "data": {
          "simplefield": "Form data filled from backend"
        }
      }
    }
  }
}
