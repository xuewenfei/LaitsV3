
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Tutorial: Hello Dojo!</title>
    </head>
    <body>
        <h1>Object Store Demonstration</h1>

        <!--hidden form to submit POST variables in JavaScript below-->
        <form id="sampleForm" name="sampleForm" method="post" action="../../author-load-save.php">
            <input type="hidden" name="action" id="action" value="">
            <input type="hidden" name="id" id="id" value="">
            <input type="hidden" name="section" id="section" value="">
            <input type="hidden" name="problem" id="problem" value="">
            <input type="hidden" name="saveData" id="saveData" value="">
        </form>

        <!-- load dojo and provide config via data attribute -->
        <script src="//ajax.googleapis.com/ajax/libs/dojo/1.8.5/dojo/dojo.js" data-dojo-config="async: true"></script>

        <script>
            require(["/laits/js/json/model", "/laits/js/json/pedagogical_model"], function(model, pm) {


                //The next three lines show loading a model from a JSON formated string
                var loadModel = new model(null, null, null, null, null, null);
                var string = '{ "task": { "taskName": "Rabbits - Intro Problem", "properties": { "phase": "intro", "type": "construct", "URL": "images/rabbit.jpeg", "startTime": 0, "endTime": 10, "timeStep": 1, "units": "years" }, "taskDescription": "In this exercise, you will construct a model of how a rabbit population grows when no rabbits die. The first quantity in this model is the population or number of rabbits in the population. Initially, there are 100 rabbits, but the number increases with time. The new population each month is its present value plus the number of births (number of rabbits born each month). The number of births is equal to the product of the population and the birth rate. The birthrate or the ratio of the number of rabbits born in a month to the rabbit population that month has a fixed value of 0.2.", "givenModelNodes": [ { "ID": "id1", "name": "population", "type": "accumulator", "parentNode": false, "extra": false, "order": 1, "units": "rabbits", "inputs": [ { "ID": "id2" } ], "initial": 100, "equation": "+ id2", "correctDesc": "The number of rabbits in the population", "attemptCount": { "description": 2, "type": 1, "initial": 2, "units": 4, "equation": 3 }, "status": { "description": "correct", "type": "demo", "initial": "correct", "units": "demo", "equation": "correct" } }, { "ID": "id2", "name": "births", "type": "function", "parentNode": true, "extra": false, "order": 2, "units": "births", "inputs": [ { "ID": "id1" }, { "ID": "id3" } ], "initial": null, "equation": "id1 * id3", "correctDesc": "The number of rabbits born each month", "attemptCount": { "description": 2, "type": 1, "initial": 0, "units": 2, "equation": 1 }, "status": { "description": "demo", "type": "demo", "initial": "null", "units": "correct", "equation": "correct" } }, { "ID": "id3", "name": "birth rate", "type": "parameter", "parentNode": false, "extra": false, "order": 3, "units": "percent", "inputs": [], "initial": null, "equation": ".2", "correctDesc": "The ratio of number of rabbits born in a month to the rabbit population that month", "attemptCount": { "description": 1, "type": 1, "initial": 0, "units": 1, "equation": 3 }, "status": { "description": "correct", "type": "correct", "initial": "null", "units": "correct", "equation": "correct" } } ], "studentModelNodes": [ { "ID": "id1", "name": "population", "inGivenModel": true, "inputs": [ { "ID": "id2" } ], "position": { "x": 100, "y": 100 }, "studentSelections": { "description": "The number of rabbits in the population", "type": "accumulator", "initial": 100, "units": "rabbits", "equation": "+ id2" } }, { "ID": "id2", "name": "births", "inGivenModel": true, "inputs": [ { "ID": "id1" }, { "ID": "id3" } ], "position": { "x": 300, "y": 100 }, "studentSelections": { "description": "The number of rabbits born each month", "type": "function", "initial": "null", "units": "births", "equation": "id1 * id3" } }, { "ID": "id3", "name": "birth rate", "inGivenModel": true, "inputs": [], "position": { "x": 500, "y": 100 }, "studentSelections": { "description": "The ratio of number of rabbits born in a month to the rabbit population that month", "type": "parameter", "initial": "null", "units": "percent", "equation": ".2" } } ] } }';
                loadModel.loadModel(string);
//                alert(loadModel.getURL());
                //});

                // The next section builds a model from scratch as would happen as an author creates a problem
                var rabbits = new model("Rabbits - Intro Problem", "images/rabbit.jpeg", 0, 10, 1, "years");
                rabbits.setTaskDescription("In this exercise, you will construct a model of how a rabbit population grows when no rabbits die. The first quantity in this model is the population or number of rabbits in the population. Initially, there are 100 rabbits, but the number increases with time. The new population each month is its present value plus the number of births (number of rabbits born each month).  The number of births is equal to the product of the population and the birth rate. The birthrate or the ratio of the number of rabbits born in a month to the rabbit population that month has a fixed value of 0.2.");
                rabbits.setPhase("intro");
                rabbits.setType("construct");
                var rabbitID = rabbits.addNode();
                rabbits.setNodeName(rabbitID, "population");
                rabbits.setNodeParent(rabbitID, false);
                rabbits.setNodeType(rabbitID, "accumulator");
                rabbits.setNodeExtra(rabbitID, false);
                rabbits.setNodeUnits(rabbitID, "rabbits");
                rabbits.setNodeInitial(rabbitID, 100);
                rabbits.setNodeEquation(rabbitID, "+ id2");
                rabbits.setNodeCorrectDesc(rabbitID, "The number of rabbits in the population");
                rabbits.addNodeWithAttributes("births", true, "function", false, "births", null, "id1 * id3", "The number of rabbits born each month");
                rabbits.addNodeWithAttributes("birth rate", false, "parameter", false, "percent", null, ".2", "The ratio of number of rabbits born in a month to the rabbit population that month");
                rabbits.addNodeInput(rabbits.getNodeIDByName("births"), rabbits.getNodeIDByName("population"));
                rabbits.addNodeInput(rabbits.getNodeIDByName("population"), rabbits.getNodeIDByName("births"));
                rabbits.addNodeInput(rabbits.getNodeIDByName("birth rate"), rabbits.getNodeIDByName("births"));
                rabbits.setNodeParent("id2", true);


                rabbits.addExtraDescription("The number of rabbits in the population during the second month", "model");
                rabbits.addExtraDescription("The ratio of rabbits born with superpowers to ordinary rabbits", "extra");

                //************ Using the Pedagogical Model ************
                var ped = new pm("coached", rabbits);
                var descInfo, typeInfo, unitsInfo, inputsInfo;

                //Set description
                console.log("The ratio of number of rabbits born in a month to the rabbit population that month");
                descInfo = ped.descriptionAction("The ratio of number of rabbits born in a month to the rabbit population that month");
                console.log("******" + descInfo.message + "******\n");

                console.log("The ratio of rabbits born with superpowers to ordinary rabbits");
                descInfo = ped.descriptionAction("The ratio of rabbits born with superpowers to ordinary rabbits");
                console.log("******" + descInfo.message + "******\n");

                console.log("The number of rabbits born each month");
                descInfo = ped.descriptionAction("The number of rabbits born each month");
                console.log("******" + descInfo.message + "******\n");

                //Set type
                console.log("sum");
                typeInfo = ped.typeAction(descInfo.ID, "sum");
                console.log("******" + typeInfo.message + "******\n");

                console.log("product");
                typeInfo = ped.typeAction(descInfo.ID, "product");
                console.log("******" + typeInfo.message + "******\n");

                //Set units
                console.log("rabbits");
                unitsInfo = ped.unitsAction(typeInfo.ID, "rabbits");
                console.log("******" + unitsInfo.message + "******\n");

                console.log("births");
                unitsInfo = ped.unitsAction(typeInfo.ID, "births");
                console.log("******" + unitsInfo.status + "******\n");

                //Set inputs
                console.log("The ratio of rabbits born with superpowers to ordinary rabbits");
                inputsInfo = ped.inputsAction(unitsInfo.ID, "The ratio of rabbits born with superpowers to ordinary rabbits");
                console.log("******" + inputsInfo.message + "******\n");
                
                console.log("A bunch of rabbits");
                inputsInfo = ped.inputsAction(unitsInfo.ID, "A bunch of rabbits");
                console.log("******" + inputsInfo.message + "******\n");
                
                console.log("The number of rabbits in the population");
                inputsInfo = ped.inputsAction(unitsInfo.ID, "The number of rabbits in the population");
                console.log("******" + inputsInfo.message + "******\n");
                alert(inputsInfo.status); 




                //*****This builds the student model
//                rabbits.addStudentNodeWithName("population");
//                rabbits.addStudentNodeWithName("births");
//                rabbits.addStudentNodeWithName("birth rate");
//                rabbits.addStudentNodeInput(rabbits.getNodeIDByName("births"), rabbits.getNodeIDByName("population"));
//                rabbits.addStudentNodeInput(rabbits.getNodeIDByName("population"), rabbits.getNodeIDByName("births"));
//                rabbits.addStudentNodeInput(rabbits.getNodeIDByName("birth rate"), rabbits.getNodeIDByName("births"));
//                rabbits.setStudentNodeSelection("id1", "description", "The number of rabbits born each month");
//                rabbits.setStudentNodeSelection("id1", "description", "The ratio of number of rabbits born in a month to the rabbit population that month");
//                rabbits.setStudentNodeSelection("id1", "type", "function");
//                rabbits.setToDemo("id1", "type");
//                rabbits.setStudentNodeSelection("id1", "initial", 10);
//                rabbits.setStudentNodeSelection("id1", "initial", 100);
//                rabbits.setStudentNodeSelection("id1", "units", "monkeys");
//                rabbits.setStudentNodeSelection("id1", "units", "cowboys");
//                rabbits.setStudentNodeSelection("id1", "units", "giraffes");
//                rabbits.setStudentNodeSelection("id1", "units", "bunnies");
//                rabbits.setToDemo("id1", "units");
//                rabbits.setStudentNodeSelection("id1", "equation", "id2");
//                rabbits.setStudentNodeSelection("id1", "equation", "10 + id2");
//                rabbits.setStudentNodeSelection("id1", "equation", "+ id2");
//
//                rabbits.setStudentNodeSelection("id2", "description", "The number of rabbits in the population");
//                rabbits.setStudentNodeSelection("id2", "description", "The ratio of number of rabbits born in a month to the rabbit population that month");
//                rabbits.setToDemo("id2", "description");
//                rabbits.setStudentNodeSelection("id2", "type", "parameter");
//                rabbits.setToDemo("id2", "type");
//                rabbits.setStudentNodeSelection("id2", "units", "rabbits");
//                rabbits.setStudentNodeSelection("id2", "units", "births");
//                rabbits.setStudentNodeSelection("id2", "equation", "id1 * id3");                
//
////                rabbits.setStudentNodeSelection("id3", "description", "The ratio of number of rabbits born in a month to the rabbit population that month");
////                rabbits.setStudentNodeSelection("id3", "type", "parameter");
////                rabbits.setStudentNodeSelection("id3", "units", "percent");
////                rabbits.setStudentNodeSelection("id3", "equation", "20");
////                rabbits.setStudentNodeSelection("id3", "equation", "2");
////                rabbits.setStudentNodeSelection("id3", "equation", ".2");
////                rabbits.addExtraDescription("The number of rabbits in the population during the second month", "model");
////                rabbits.addExtraDescription("The ratio of rabbits born with superpowers to ordinary rabbits", "extra");




                //alert(rabbits.getExtraDescriptions(null));


                //The next section prints the entire model on the screen, and then uses several getters to access the models information

//                alert(rabbits.getPhase());
//                alert(rabbits.getType());
//                alert(rabbits.getTaskName());
//                alert(rabbits.getURL());
//                alert(rabbits.getStartTime());
//                alert(rabbits.getEndTime());
//                alert(rabbits.getTimeStep());
//                alert(rabbits.getUnits());
//                alert(rabbits.getTaskDescription());
                //alert(rabbits.getNodeIDByName("birth rate"));


//                var i = rabbits.setStudentNodeName("id2", "John");
//                rabbits.setStudentNodeName(i, "births");
                document.write(JSON.stringify(rabbits.model, null, 4));
//                document.write(JSON.stringify(loadModel.model, null, 4));
//                alert(rabbits.getNodeAttemptCount("id1", "type"));

                //alert("Order of " + rabbits.getNodeIDByName("birth rates") + ": " + rabbits.getNodeOrder(rabbits.getNodeIDByName("birth rates")));

            });
        </script>

    </body>

</html>
