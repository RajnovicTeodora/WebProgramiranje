
var registration_url = ".../services/rest/registration/add";

function readURL(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();

		reader.onload = function(e) {
			$('#blah')
				.attr('src', e.target.result)
				.width(150)
				.height(200);
		};

		reader.readAsDataURL(input.files[0]);
	}
}

$("#manifestation_form").submit(function (event) {

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
		$('#error').text('All fields must be filled!');
		$("#error").show().delay(3000).fadeOut();
		return;
	}

	var today = new Date().toISOString().split("T")[0];;
	if (birthday >= today) {
		$('#error').text('Date of birth must be before today\'s date');
		$("#error").show().delay(3000).fadeOut();
		return;
	}
	$.ajax({
		type: 'POST',
		url: "rest/registration/add",
		data: JSON.stringify({
			username: username,
			password: password,
			name: name,
			surname: surname,
			gender: gender,
			birthday: birthday

		}),
		contentType: 'application/json',
		success: function (result) {
			console.log(result);
			M.toast({ html: 'Successfully registered', classes: 'rounded', panning: 'center' });

		},
		error: function () {
			$('#error').text("Invalid input!");
			$("#error").show().delay(2000).fadeOut();
		}
	});
});

$(document).ready(function() {
	var date = new Date();
  	date.setDate(date.getDate() + 1);
  
	document.getElementById('date').min = date.toISOString().substr(0, 16)
});