var dimension=4;
$(document).ready(function(){
	aplicarPuzzle(puzzleActual);
	$("#random").click(function(){
		$.ajax({
			url: "rest/random/"+dimension,
			dataType: "json",
			type: "GET",
			success: function(response){
				aplicarPuzzle(response);
			},
			error: function(rs, e){
				console.log("Error"+rs.responseText);
			}
		});
	});

	if($("#d3").attr("checked")){
		dimension = $("#d3").val();
		createDefaultPuzzle();    
	}
	if($("#d4").attr("checked")){
		dimension = $("#d4").val();
		createDefaultPuzzle();

	}


	$("input[name=dimension]:radio").change(function(){
		if($("#d3").attr("checked")){
			dimension = $(this).val();
			createDefaultPuzzle();

		}
		if($("#d4").attr("checked")){
			dimension = $(this).val();
			createDefaultPuzzle();

		}
	});
	$("#solve").click(function(){
		$.ajax({
			url: "rest/solve",
			dataType: "json",
			type: "POST",
			data:{
				dimension: dimension,
				heuristic: $("#heuristic").val(),
				puzzle:"["+puzzleActual.toString()+"]"
			},
			beforeSend: function(){
				$("#status").html("Buscando solución "+"<img style='width:20px;' src='http://devcomponents.com/blog/wp-content/uploads/CircularProgressAnimation.gif'></img>");
			},
			success: function(response){
				if(response.estado){
					$(".disable").attr("disabled", "disabled");
					$("#pasos").html(response.pasos);
					$("#status").html(response.statusMessage);
					resolver(0, response.solution);
				}
				else{
					$("#status").html(response.statusMessage);
				}
			},
			error: function(rs, e){
				console.log("Error"+rs.responseText);
			}
		});
	});
});


var pasos = [
 [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
]

var puzzleActual=[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0];
function aplicarPuzzle(puzzle){
	$("#puzzle").html("");
	puzzle.forEach(function(value,index, array){
		if(value != 0){
			$("<div class='tile'>"+value+"</div>").appendTo("#puzzle");
		}else{
			$("<div class='tile empty'></div>").appendTo("#puzzle");
		}
	});
	puzzleActual = puzzle;
}

function resolver(i, pasos){
	if(i < pasos.length - 1){
		valorASustituir = pasos[i+1][pasos[i].indexOf(0)];

		$(".tile").each(function(index, value){
			if($(value).html() == String(valorASustituir)){
				fila = Math.trunc(index / dimension);
				columna = index % dimension;
				ancho = fila / dimension * $("#puzzle").width();
				alto = columna / dimension * $("#puzzle").height();
				columnaCero = pasos[i].indexOf(0) % dimension;
				filaCero = Math.trunc(pasos[i].indexOf(0) / dimension);

				$("<div class='empty helper' style='position: absolute; top: "+ancho+"px; left: "+alto+"px;''></div>").appendTo("#puzzle");
				$("<div id='slideHelper' class='tile helper' style='top: "+ancho+"px; left: "+alto+"px;'>"+$(value).html()+"</div>").appendTo("#puzzle");

				var options = {};
				if(fila != filaCero){

					options.top= fila < filaCero ? "+="+$("#puzzle").width()/dimension : "-="+$("#puzzle").width()/dimension;

				}
				if(columna != columnaCero){
					options.left= columna < columnaCero ? "+="+$("#puzzle").height()/dimension : "-="+$("#puzzle").height()/dimension;
				}


				$("#slideHelper").animate(options,1500, "swing", function(){
					$(".helper").remove();
					$("#slideHelper").remove();

					$("#puzzle").html("");
					for (var j = 0;j<pasos[i+1].length;j++) {
						value = pasos[i+1][j];
						if(value != 0){
							$("<div class='tile'>"+value+"</div>").appendTo("#puzzle");
						}else{
							$("<div class='tile empty'></div>").appendTo("#puzzle");
						}
					};
					resolver(i+1, pasos);
				});

			}
		});
	}else{
		$(".disable").removeAttr("disabled");
		puzzleActual = pasos[i];
	}
}

function createDefaultPuzzle(){
	array = [];
	for(var i = 0; i < dimension * dimension - 1; i++){
		array[i] = i+1;
	}
	array[i] = 0;
	puzzleActual=array;
	aplicarPuzzle(puzzleActual);
	if(dimension == 3){
		$("#puzzle").height(249);
		$("#puzzle").width(249);
	}else{
		$("#puzzle").height(332);
		$("#puzzle").width(332);
	}
}

