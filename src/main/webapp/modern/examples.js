$(document).ready(function() {

// the basics
// ----------

	$("#nationwidev").hide();
	$("#statewidev").hide();
	$("#citywidev").hide();
	
	$("#treatid").keyup(function(e){
		if(e.keyCode == 13) {
			gQueryStr = $("#treatid").val();
			var initialurlvalforcode = '/healthcost-webapp-v1/getcostbycode?code='+codesAndDescJsonObj[gQueryStr];
			$.ajax({
				dataType: "json",
				url: initialurlvalforcode,
				data: '',
				success: function(resp) {
					//alert(resp.code+" : "+resp.desc+" : "+resp.avg+" : "+resp.low+" : "+resp.high);
					var initialurlvalforcodeandstate = '/healthcost-webapp-v1/getstateswithcode?code='+codesAndDescJsonObj[gQueryStr];
					$.ajax({
						dataType: "json",
						url: initialurlvalforcodeandstate,
						data: '',
						success: function(respobj) {
							var htmlString = '<p class="example-description">Nationwide Cost</p><p class="example-description">' + 
							'<span><b>Lowest Charged Amount</b></span> : '+resp.low+' &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><b>Highest Charged Amount</b></span> : '+resp.high+'</p>';
							$('#stateid').find('option').remove().end().append($("<option></option>").attr("value","--").text("select a state"));							
					    	jQuery.each(respobj, function(i, val) {
					    		 $('#stateid').append($("<option></option>").attr("value",i).text(val)); 
					    	});							
							$("#nationwidevdata").html(htmlString);
							$("#nationwidev").show();
							$("#statewidev").hide();
							$("#citywidev").hide();
						}
					});
				}
			});			
		}
	});	

	$( "#stateid" ).change(function() {
			gQueryStr = $("#treatid").val();
			var initialurlvalforstate = '/healthcost-webapp-v1/getcostbystate?code='+codesAndDescJsonObj[gQueryStr]+'&state='+ $("#stateid").val();
			$.ajax({
				dataType: "json",
				url: initialurlvalforstate,
				data: '',
				success: function(resp) {
					//alert(resp.code+" : "+resp.desc+" : "+resp.state+" : "+resp.avg+" : "+resp.low+" : "+resp.high);
					var initialurlvalforcodeandstate = '/healthcost-webapp-v1/getcitywithcode?code='+codesAndDescJsonObj[gQueryStr]+'&state='+$("#stateid").val();
					$.ajax({
						dataType: "json",
						url: initialurlvalforcodeandstate,
						data: '',
						success: function(respobj) {
							var htmlString = '<p class="example-description">Statewide Cost - '+$("#stateid").val()+'</p><p class="example-description">' + 
							'<span><b>Lowest Charged Amount</b></span> : '+resp.low+' &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><b>Highest Charged Amount</b></span> : '+resp.high+'</p>';
							$('#cityid').find('option').remove().end().append($("<option></option>").attr("value","--").text("select a city"));							
					    	jQuery.each(respobj, function(i, val) {
					    		 $('#cityid').append($("<option></option>").attr("value",i).text(val)); 
					    	});							
							$("#statewidevdata").html(htmlString);
							$("#statewidev").show();
							$("#citywidev").hide();
						}
					});					
					
				}
			});		  
	});

	$( "#cityid" ).change(function() {
		gQueryStr = $("#treatid").val();
		var initialurlvalforstate = '/healthcost-webapp-v1/getcostbycity?code='+codesAndDescJsonObj[gQueryStr]+'&state='+ $("#stateid").val()+'&city='+ $("#cityid").val();
		$.ajax({
			dataType: "json",
			url: initialurlvalforstate,
			data: '',
			success: function(resp) {
				//alert(resp.code+" : "+resp.desc+" : "+resp.state+" : "+resp.avg+" : "+resp.low+" : "+resp.high);
				var htmlString = '<p class="example-description">Citywide Cost - '+$("#cityid").val()+", "+$("#stateid").val()+'</p><p class="example-description">' + 
				'<span><b>Lowest Charged Amount</b></span> : '+resp.low+' &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><b>Highest Charged Amount</b></span> : '+resp.high+'</p>';
				$("#citywidevdata").html(htmlString);
				$("#citywidev").show();
			}
		});		  
	});
	
var initialurlval = '/healthcost-webapp-v1/getallcodes';
$.ajax({
	dataType: "json",
	url: initialurlval,
	data: '',
	success: function(resp) {
		codesAndDescJsonObj = resp;
		var states = [];
		
    	jQuery.each(resp, function(i, val) {
    		states.push(i);
    	});
    	
var substringMatcher = function(strs) {
  return function findMatches(q, cb) {
    var matches, substringRegex;

    // an array that will be populated with substring matches
    matches = [];

    // regex used to determine if a string contains the substring `q`
    substrRegex = new RegExp(q, 'i');

    // iterate through the pool of strings and for any string that
    // contains the substring `q`, add it to the `matches` array
    $.each(strs, function(i, str) {
      if (substrRegex.test(str)) {
        // the typeahead jQuery plugin expects suggestions to a
        // JavaScript object, refer to typeahead docs for more info
        matches.push({ value: str });
      }
    });

    cb(matches);
  };
};
/**
states = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California',
  'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii',
  'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana',
  'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota',
  'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire',
  'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
  'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island',
  'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
  'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
];

alert(states);
**/

$('#the-basics .typeahead').typeahead({
  hint: true,
  highlight: true,
  minLength: 1
},
{
  name: 'states',
  displayKey: 'value',
  source: substringMatcher(states)
});

// bloodhound
// ----------

// constructs the suggestion engine
var states = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  // `states` is an array of state names defined in "The Basics"
  local: $.map(states, function(state) { return { value: state }; })
});

// kicks off the loading/processing of `local` and `prefetch`
states.initialize();

$('#bloodhound .typeahead').typeahead({
  hint: true,
  highlight: true,
  minLength: 1
},
{
  name: 'states',
  displayKey: 'value',
  // `ttAdapter` wraps the suggestion engine in an adapter that
  // is compatible with the typeahead jQuery plugin
  source: states.ttAdapter()
});

// prefetch
// --------

var countries = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  limit: 10,
  prefetch: {
    // url points to a json file that contains an array of country names, see
    // https://github.com/twitter/typeahead.js/blob/gh-pages/data/countries.json
    url: '../data/countries.json',
    // the json file contains an array of strings, but the Bloodhound
    // suggestion engine expects JavaScript objects so this converts all of
    // those strings
    filter: function(list) {
      return $.map(list, function(country) { return { name: country }; });
    }
  }
});

// kicks off the loading/processing of `local` and `prefetch`
countries.initialize();

// passing in `null` for the `options` arguments will result in the default
// options being used
$('#prefetch .typeahead').typeahead(null, {
  name: 'countries',
  displayKey: 'name',
  // `ttAdapter` wraps the suggestion engine in an adapter that
  // is compatible with the typeahead jQuery plugin
  source: countries.ttAdapter()
});

// remote
// ------

var bestPictures = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  prefetch: '../data/films/post_1960.json',
  remote: '../data/films/queries/%QUERY.json'
});

bestPictures.initialize();

$('#remote .typeahead').typeahead(null, {
  name: 'best-pictures',
  displayKey: 'value',
  source: bestPictures.ttAdapter()
});

// custom templates
// ----------------

$('#custom-templates .typeahead').typeahead(null, {
  name: 'best-pictures',
  displayKey: 'value',
  source: bestPictures.ttAdapter(),
  templates: {
    empty: [
      '<div class="empty-message">',
      'unable to find any Best Picture winners that match the current query',
      '</div>'
    ].join('\n'),
    suggestion: Handlebars.compile('<p><strong>{{value}}</strong> – {{year}}</p>')
  }
});

// multiple datasets
// -----------------

var nbaTeams = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('team'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  prefetch: '../data/nba.json'
});

var nhlTeams = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('team'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  prefetch: '../data/nhl.json'
});

nbaTeams.initialize();
nhlTeams.initialize();

$('#multiple-datasets .typeahead').typeahead({
  highlight: true
},
{
  name: 'nba-teams',
  displayKey: 'team',
  source: nbaTeams.ttAdapter(),
  templates: {
    header: '<h3 class="league-name">NBA Teams</h3>'
  }
},
{
  name: 'nhl-teams',
  displayKey: 'team',
  source: nhlTeams.ttAdapter(),
  templates: {
    header: '<h3 class="league-name">NHL Teams</h3>'
  }
});

// scrollable dropdown menu
// ------------------------

$('#scrollable-dropdown-menu .typeahead').typeahead(null, {
  name: 'countries',
  displayKey: 'name',
  source: countries.ttAdapter()
});

var arabicPhrases = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('word'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  local: [
    { word: "الإنجليزية" },
    { word: "نعم" },
    { word: "لا" },
    { word: "مرحبا" },
    { word: "أهلا" }
  ]
});

arabicPhrases.initialize();

$('#rtl-support .typeahead').typeahead({
  hint: false
},
{
  name: 'arabic-phrases',
  displayKey: 'word',
  source: arabicPhrases.ttAdapter()
});


	}
});

	
	});
