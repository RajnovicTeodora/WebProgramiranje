$("#registration_form").submit(function(event) {

	// Stop form from submitting normally
	event.preventDefault();
	console.log("Registration...");

	let username = $('input[name="username"]').val();
	let password = $('input[name="password"]').val();
	let name = $('input[name="name"]').val();
	let surname = $('input[name="surname"]').val();
	let gender = document.querySelector('input[name="gender"]:checked').value;
	let birthday = $('input[name="date"]').val();

	if (!username || !password || !name || !surname || !gender || !birthday) {
		M.toast({ html: 'All fields must be filled!', classes: 'rounded', panning: 'center' });
		return;
	}

	var today = new Date().toISOString().split("T")[0];
	if (birthday >= today) {
		M.toast({ html: 'Date of birth must be before today\'s date', classes: 'rounded', panning: 'center' });
		return;
	}
	$.ajax({
		type: 'POST',
		url: "rest/registration/add",
		data: JSON.stringify({
			username: username,
			password: password,
			name: name,
			lastName: surname,
			gender: gender,
			birthday: birthday

		}),
		contentType: 'application/json',
		success: function(result) {
			console.log(result);
			M.toast({ html: 'Successfully registered', classes: 'rounded', panning: 'center' });

			$.ajax({
				type: 'GET',
				contentType: 'application/json',
				url: 'rest/registration/registeredUser',
				success: function(user) {
					if (user != null && user.role == "ADMINISTRATOR")
						window.location.href = "http://localhost:8080/RESTApp/users.html";
					else
						window.location.href = "http://localhost:8080/RESTApp/index.html";
				}
			});

		},
		error: function(err) {
			if (err.status == 403)
				M.toast({ html: 'Username already exists', classes: 'rounded', panning: 'center' });
			else
				M.toast({ html: 'Invalid input', classes: 'rounded', panning: 'center' });
		}
	});
});

$(document).ready(function() {
	var date = new Date();
	date.setDate(date.getDate() - 1);

	document.getElementById('date').max = date.toISOString().substr(0, 10)
	$.ajax({
		type: 'GET',
		contentType: 'application/json',
		url: 'rest/registration/registeredUser',
		success: function(user) {
			if (user != null) {
				if (user.role == "ADMINISTRATOR") {
					document.getElementById("li_login").innerHTML = ''
					document.getElementById("li_registration").innerHTML = ''
				} else {
					document.getElementById("li_users").innerHTML = ''
					document.getElementById("li_my_profile").innerHTML = ''
					document.getElementById("li_tickets").innerHTML = ''
					document.getElementById("li_manifestations").innerHTML = ''
					document.getElementById("li_logout").innerHTML = ''
				}
			} else {
				document.getElementById("li_users").innerHTML = ''
				document.getElementById("li_my_profile").innerHTML = ''
				document.getElementById("li_tickets").innerHTML = ''
				document.getElementById("li_manifestations").innerHTML = ''
				document.getElementById("li_logout").innerHTML = ''
			}

		},
		error: function() {

			document.getElementById("li_users").innerHTML = ''
			document.getElementById("li_my_profile").innerHTML = ''
			document.getElementById("li_tickets").innerHTML = ''
			document.getElementById("li_manifestations").innerHTML = ''
			document.getElementById("li_logout").innerHTML = ''
		}
	});

});