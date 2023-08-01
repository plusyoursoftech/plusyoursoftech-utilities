YUI().use('node', 'event', 'form-validator', function(Y) {
  var form = Y.one('#myForm');
  var submitButton = Y.one('#submitButton');

  var validator = new Y.FormValidator({
    form: form,
    fieldStrings: {
      name: 'Name',
      email: 'Email',
      password: 'Password'
    },
    rules: {
      name: 'required',
      email: 'required,email',
      password: 'required,minlength[6]'
    }
  });

  // Validate the form on button click
  submitButton.on('click', function(e) {
    e.preventDefault();
    clearErrorStyles(); // Clear any existing error styles
    if (validator.validateForm()) {
      submitForm();
    } else {
      displayErrors();
    }
  });

  // Submit the form if it's valid
  function submitForm() {
    // Perform any additional processing here if needed
    form.submit();
  }

  // Display error messages and add red border to input fields with errors
  function displayErrors() {
    var errors = validator.getErrors();
    for (var field in errors) {
      var errorMessage = errors[field];
      var inputField = Y.one('#' + field);
      inputField.setStyle('border', '1px solid red');
    }
  }

  // Clear error styles when the form is revalidated
  function clearErrorStyles() {
    var inputFields = form.all('input');
    inputFields.each(function(inputField) {
      inputField.setStyle('border', '1px solid #ccc');
    });
  }
});
