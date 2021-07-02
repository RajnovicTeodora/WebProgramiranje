function addUserTr(user) {

	let tr = $('<tr id="' + user.username + '"></tr>');
	let tdUsername = $('<td>' + user.username + '</td>');
	let tdName = $('<td>' + user.firstName + '</td>');
	let tdSurname = $('<td>' + user.lastName + '</td>');
	let tdGender = $('<td>' + user.gender + '</td>');
	let tdBirthday = $('<td>' + user.birthday + '</td>');
	let tdRole = $('<td>' + user.role + '</td>');
	let tdType = $('<td>' + user.customerType + '</td>');
	let tdPoints = $('<td>' + user.points + '</td>');


	tr.append(tdUsername).append(tdName).append(tdSurname).append(tdGender).append(tdBirthday).append(tdRole).append(tdType).append(tdPoints);

	$('#users tbody').append(tr);
}

function blockUser(user) {

	let username = user.id;
	console.log(username);

	$.ajax({
		type: 'POST',
		url: "rest/admin/blockUser",
		data: username,
		contentType: 'application/json',
		success: function(result) {
			window.location.reload(false)
		},
		error: function() {
			M.toast({ html: 'Failed to block user!', classes: 'rounded', panning: 'center' });
		}
	});
}

function deleteUser(user) {

	let username = user.id;
	console.log(username);

	$.ajax({
		type: 'POST',
		url: "rest/admin/deleteUser",
		data: username,
		contentType: 'application/json',
		success: function(result) {
			window.location.reload(false)
		},
		error: function() {
			M.toast({ html: 'Failed to delete user!', classes: 'rounded', panning: 'center' });
		}
	});
}

function addUserTrAdmin(user) {

	$("#sus").show();
	$("#blockBtn").show();
	$("#deleteBtn").show();

	let tr = $('<tr id="' + user.username + '"></tr>');
	let tdUsername = $('<td>' + user.username + '</td>');
	let tdName = $('<td>' + user.firstName + '</td>');
	let tdSurname = $('<td>' + user.lastName + '</td>');
	let tdGender = $('<td>' + user.gender + '</td>');
	let tdBirthday = $('<td>' + user.birthday + '</td>');
	let tdRole = $('<td>' + user.role + '</td>');
	let tdType = $('<td>' + user.customerType + '</td>');
	let tdPoints = $('<td>' + user.points + '</td>');

	let susIcon = $('<td></td>');
	let delBtn = $('<td><a id="deleteButton" onClick="deleteUser('+user.username+')" class="btn-floating btn-medium waves-effect waves-light red"><i class="material-icons">delete</i></a></td>');
	let blockBtn = $('<td><a id="blockButton" onClick="blockUser('+user.username+')" class="btn-floating btn-medium waves-effect waves-light"><i class="material-icons">block</i></a></td>');


	if(user.isSus == "Suspicious")
		susIcon = $('<td><i class="material-icons medium red-text">error_outline</i></td>');

	if(user.status == "blocked"){
		blockBtn = $('<td><a id="blockButton" onClick="blockUser('+user.username+')" class="btn-floating btn-medium waves-effect waves-light grey"><i class="material-icons">block</i></a></td>');
	}
	if(user.status == "deleted"){
		delBtn = $('<td>Deleted user</td>');
		blockBtn = $('<td></td>');
	}
	if(user.role == "Administrator"){
		blockBtn = $('<td></td>');
		delBtn = $('<td></td>');
	}
	if(user.role == "User"){
		delBtn = $('<td></td>');
	}
	
	
	tr.append(tdUsername).append(tdName).append(tdSurname).append(tdGender).append(tdBirthday).append(tdRole).append(tdType).append(tdPoints).append(susIcon).append(blockBtn).append(delBtn);

	$('#users tbody').append(tr);
}

function showInfo(user) {

	if (!user) return;
	console.log(user);

	$.ajax({
		type: 'GET',
		url: 'rest/registration/users',
		success: function(users) {
			if (user.role == "VENDOR")
				document.getElementById("new_vendor").innerHTML = ''
			for (let u of users) {
				if (user.role == "VENDOR"){
					addUserTr(u);
				}
				else if (user.role == "ADMINISTRATOR")
					addUserTrAdmin(u);
			}
		}
	});

}

$(document).ready(function() {
	$.get({
		url: 'rest/registration/registeredUser',
		success: function(user) {
			if (user.role == "ADMINISTRATOR" || user.role == "VENDOR")
				showInfo(user);
		}
	});
});

$("#filter_users_form").submit(function(event) {

	let userRole = 'admin';

	$.get({
		url: 'rest/registration/registeredUser',
		success: function(user) {
			if (user.role != "ADMINISTRATOR")
				userRole = 'notAdmin';
		}
	});

	event.preventDefault();
	console.log("Filtering users...");
	
	let firstName = $('input[name="firstName"]').val();
	let lastName = $('input[name="lastName"]').val();
	let username = $('input[name="username"]').val();

	let type = document.getElementById('typeSelect').value;
	let role = document.getElementById('roleSelect').value;

	
	if(firstName==="") firstName="null";
	if(lastName==="") lastName="null";
	if(username==="") username="null";
	
	$.ajax({
 		type: 'GET',
 		url: "rest/registration/searchUsers/"+firstName+"/"+lastName+"/"+username+"/"+type+"/"+role,
		contentType: 'application/json',
 		success: function(response) {
			$('#users tbody').empty();
			for (let user of response) {
				if(userRole == 'admin'){
					addUserTrAdmin(user);
				}
				else{
					addUserTr(user);
				}
			}
 		},
 		error: function() {
 			M.toast({ html: 'Failed to send data', classes: 'rounded', panning: 'center' });
 		}
	});	
 });