
$("#logout-click").click(function(event) {
	event.preventDefault();
	$.ajax({
		type: 'GET',
		url: "rest/registration/logout",
		contentType: 'application/json',
		success: function(result) {
			M.toast({ html: 'Successfully logged out', classes: 'rounded', panning: 'center' });
			window.location.href = "http://localhost:8080/RESTApp/index.html";

		}
	});

});



$("#login_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Log in...");

	let username = $('input[name="username"]').val();
	let password = $('input[name="password"]').val();

	if (!username || !password) {
		M.toast({ html: 'All fields must be filled out', classes: 'rounded', panning: 'center' });
		return;
	}

	$.ajax({
		type: 'POST',
		url: "rest/registration/login",
		data: JSON.stringify({
			username: username,
			password: password

		}),
		contentType: 'application/json',
		success: function(result) {
			console.log(result);
			M.toast({ html: 'Successfully logged in', classes: 'rounded', panning: 'center' });
			window.location.href = "http://localhost:8080/RESTApp/index.html";

		},
		error: function() {

			M.toast({ html: 'Invalid username or password.' + request, classes: 'rounded', panning: 'center' });
		}
	});
});