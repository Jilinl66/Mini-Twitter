var rootURL = "http://localhost:8080/api";

//Register button
$('#btnTweetSearch').click(function() {
	searchTweet($('#keyword').val());
	return false;
});

$('#keyword').keypress(function(e){
	if(e.which == 13) {
		searchTweet($('#keyword').val());
		e.preventDefault();
		return false;
    }
});

$('#btnFollowerSearch').click(function() {
	searchFollower();
	return false;
});

$('#btnFollow').click(function() {
	insertFollower($('#followedPersonId').val());
	return false;
});

$('#followedPersonId').keypress(function(e){
	if(e.which == 13) {
		insertFollower($('#followedPersonId').val());
		e.preventDefault();
		return false;
    }
});

$('#btnUnfollow').click(function() {
	deleteFollower($('#unFollowedPersonId').val());
	return false;
});

$('#unFollowedPersonId').keypress(function(e){
	if(e.which == 13) {
		deleteFollower($('#unFollowedPersonId').val());
		e.preventDefault();
		return false;
    }
});

$('#btnComputeDis').click(function() {
	computeDistance($('#endPesonId').val());
	return false;
});

$('#endPesonId').keypress(function(e){
	if(e.which == 13) {
		computeDistance($('#endPesonId').val());
		e.preventDefault();
		return false;
    }
});

$('#btnFindPairs').click(function() {
	findMostPcopular();
	return false;
});

$('#btnLogout').click(function() {
	logout();
	return false;
});

function searchTweet(keyword) {
	console.log('find tweets with keyword ' + keyword);
	$.ajax({
		type: 'GET',
		contentType: "application/json",
		url: rootURL + '/tweets' + '?keyword=' + keyword,
		dataType: "json",
		success: renderWebpage,
        error: function() {
                alert('error');
        }
	});
}

function searchFollower() {
	console.log('find followers');
	$.ajax({
		type: 'GET',
		contentType: "application/json",
		url: rootURL + '/followers',
		dataType: "json",
		success: renderWebpage,
        error: function() {
                alert('error');
        }
	});
}

function insertFollower(followedPersonId) {
	console.log('start following person '+ followedPersonId);
	$.ajax({
		type: 'POST',
		contentType: "application/json",
		url: rootURL + '/followers',
		// dataType: 'json',
		data: followedPersonId,
		success: renderWebpage,
        error: function() {
                alert('error');
        }
	});
}

function deleteFollower(followedPersonId) {
	console.log(' unfollow person '+ followedPersonId);
	$.ajax({
		type: 'DELETE',
		contentType: "application/json",
		url: rootURL + '/unfollow',
		dataType: 'text',
		data: followedPersonId,
		success: renderWebpage,
        error: function() {
                alert('error');
        }
	});
}


function computeDistance(endPersonId) {
	console.log('compute shortest istance to person '+ endPersonId);
	$.ajax({
		type: 'GET',
		contentType: "application/json",
		url: rootURL + '/distance?person2=' + endPersonId,
		dataType: 'json',
		success: renderWebpage,
        error: function() {
			alert('error');
        }
	});
}

function findMostPcopular() {
	console.log('fetching the most popular followers pairs')
	$.ajax({
		type: 'GET',
		contentType: "application/json",
		url: rootURL + '/popular',
		dataType: 'json',
		success: renderWebpage,
        error: function() {
                alert('error');
        }
	});
}

function logout() {
	console.log('Logout')
	$.ajax({
		type: 'GET',
		contentType: "application/json",
		url: 'http://localhost:8080/logout',
        error: function() {
                alert('error');
        }
	});
}

function renderWebpage(data) {
	var json = JSON.stringify(data);
	$('#response').html(json);
}