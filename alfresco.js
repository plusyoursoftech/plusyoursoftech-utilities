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

  // Display error messages for invalid fields
  function displayErrors() {
    var errors = validator.getErrors();
    for (var field in errors) {
      var errorMessage = errors[field];
      var errorSpan = Y.one('#' + field + 'Error');
      errorSpan.set('text', errorMessage);
    }
  }
});
