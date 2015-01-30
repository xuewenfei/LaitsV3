
// Set up initial variables

 // Creating a selenium client utilizing webdriverjs
var client = require('webdriverio').remote({
    //logLevel: "verbose",
    desiredCapabilities: {
        // See other browers at:
        // http://code.google.com/p/selenium/wiki/DesiredCapabilities
        browserName: 'chrome'

    }
});

// import chai assertion library
var assert = require('chai').assert;
// import dragoon test library module
var dtest = require('../dtestlib.js');
// import dragoon assertion library
var atest = require('../assertTestLib.js');
// import sync library
var sync = require('synchronize');
// import wrapper for asynchronous functions
var async = sync.asyncIt;

describe("Test student mode:", function() {

    before(async(function (done) {
            dtest.openProblem(client,[["problem","rabbits"],["mode","STUDENT"],
                                      ["section","regression-testing"],
                                      ["logging","true"]]);
    }));

    describe("Creating nodes:", function(){
        it("Should create Accumulator node - population", async(function(){
            dtest.menuCreateNode(client);
            dtest.setNodeDescription(client, "The number of rabbits in the population");
            dtest.popupWindowPressOk(client);
            dtest.setNodeType(client, "Accumulator");
            dtest.popupWindowPressOk(client);
            dtest.setNodeInitialValue(client, 24);
            dtest.setNodeUnits(client, "rabbits");     
            dtest.setNodeExpression(client, "net growth");
            dtest.checkExpression(client);
            dtest.nodeEditorDone(client);

        }));

        it("Should fill in function node - net growth", async(function(){
            dtest.openEditorForNode(client, "net growth");
            dtest.setNodeType(client, "Function");
            dtest.setNodeUnits(client, "rabbits/year");
            dtest.setNodeExpression(client, "growth rate*population");
            dtest.checkExpression(client);
            dtest.nodeEditorDone(client);

        }));

        it("Should fill in parameter node - growth rate", async(function(){
            dtest.openEditorForNode(client, "growth rate");
            dtest.setNodeType(client, "Parameter");
            dtest.setNodeInitialValue(client, 0.3);
            dtest.setNodeUnits(client, "1/year");
            dtest.nodeEditorDone(client);
            dtest.popupWindowPressOk(client);
        }));
    });
    describe("Checking node colors", function(){
        it("Nodes should have correct border and fill colors", async(function(){
            //Defines which nodes to check
            var nodesToCheck = ["population", "net growth", "growth rate"];
            //Does test for all nodes
            nodesToCheck.forEach(function(element){
                //Gets values
                var nodeBorderColor = dtest.getNodeBorderColor(client, element);
                var nodeBorderStyle = dtest.getNodeBorderStyle(client, element);
                var nodeFillColor = dtest.getNodeFillColor(client, element);
                //Asserts values
                assert(nodeBorderColor === "green",
                    "Node border color for " + element + " was " + nodeBorderColor + " instead of green");
                assert(nodeBorderStyle === "solid",
                    "Node border style for " + element + " was " + nodeBorderStyle + " instead of solid");
                assert(nodeFillColor === "green",
                    "Node fill color for " + element + " was " + nodeFillColor + " instead of green");
            });
        }));
    });
    
    describe("Checking Nodes:", function(){
        
        afterEach(async(function(){
            dtest.nodeEditorDone(client);
        }));

        it("Should have correct Accumulator values and colors", async(function(){
            var nodeName = "population"
            dtest.openEditorForNode(client, nodeName);

            atest.checkNodeValues([["nodeName", "population"],
                                    ["expectedDescription", "The number of rabbits in the population"],
                                    ["expectedNodeType", "Accumulator"],
                                    ["expectedInitialValue", "24"],
                                    ["expectedNodeUnits", "rabbits"],
                                    ["expectedExpression", "net growth"],
                                    ["expectedDescriptionColor", "green"],
                                    ["expectedTypeColor", "green"],
                                    ["expectedInitialColor", "green"],
                                    ["expectedUnitsColor", "green"],
                                    ["expectedExpressionColor", "green"]], dtest, client);
        }));

        it("Should have correct function values and colors", async(function(){
            var nodeName = "net growth"

            dtest.openEditorForNode(client, nodeName);

            atest.checkNodeValues([["nodeName", "net growth"],
                                    ["expectedDescription", "The number of additional rabbits each year"],
                                    ["expectedNodeType", "Function"],
                                    ["expectedNodeUnits", "rabbits/year"],
                                    ["expectedExpression", "growth rate*population"],
                                    ["expectedDescriptionColor", "green"],
                                    ["expectedTypeColor", "green"],
                                    ["expectedInitialColor", "gray"],
                                    ["expectedUnitsColor", "green"],
                                    ["expectedExpressionColor", "green"]], dtest, client);
        }));

        it("Should have correct parameter values and colors", async(function(){
            var nodeName = "growth rate";

            dtest.openEditorForNode(client, "growth rate");

            atest.checkNodeValues([["nodeName", "growth rate"],
                                    ["expectedDescription", "The number of additional rabbits per year per rabbit"],
                                    ["expectedNodeType", "Parameter"],
                                    ["expectedInitialValue", "0.3"],
                                    ["expectedNodeUnits", "1/year"],
                                    ["expectedDescriptionColor", "green"],
                                    ["expectedTypeColor", "green"],
                                    ["expectedInitialColor", "green"],
                                    ["expectedUnitsColor", "green"],
                                    ["expectedExpressionColor", "gray"]], dtest, client);            
        }));
    });

    after(function(done) {
        client.end();
        done();
    });
});