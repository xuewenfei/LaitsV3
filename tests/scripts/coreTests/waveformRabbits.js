
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

describe("Waveform Rabbits (student mode)", function() {

    before(async(function (done) {
            dtest.openProblem(client,[["problem","rabbits"],["mode","STUDENT"],
                                      ["section","regression-testing"],
                                      ["logging","true"],["activity","waveform"]]);
    }));

    // get first node wrong
    it("Should get the first node wrong and turn red", async(function(){
        dtest.openEditorForNode(client, "net growth");
        dtest.selectWaveform(client,"wave001");
        atest.checkNodeValue(dtest.getNodeBorderColor(client,"net growth"),"red","net growth border");
        atest.checkNodeValue(dtest.getNodeFillColor(client,"net growth"),"white","net growth fill");
    }));

    it("Should get the first node wrong again and turn yellow", async(function(){
        dtest.openEditorForNode(client, "net growth");
        dtest.selectWaveform(client,"wave019");
        atest.checkNodeValue(dtest.getNodeBorderColor(client,"net growth"),"yellow","net growth border");
        atest.checkNodeValue(dtest.getNodeFillColor(client,"net growth"),"white","net growth fill");
    }));
    // get second node right
    it("Should get the second node right and turn green", async(function(){
        dtest.openEditorForNode(client, "population");
        dtest.selectWaveform(client,"wave003");
        atest.checkNodeValue(dtest.getNodeBorderColor(client,"population"),"green","population border");
        atest.checkNodeValue(dtest.getNodeFillColor(client,"population"),"green","population fill");
    }));

    // close done window
    it("Should display and close the done message", async(function(){
        assert(dtest.isDonePopupVisible(client),"The Done hint popup is not visible, but it should be!");
        dtest.closeMenuDonePopup(client);
    }));
    
    // check for extra done window 
    it("Should show an equation and not show done hint again", async(function(){
        dtest.openEditorForNode(client, "net growth");
        dtest.clickWaveformEquation(client);
        dtest.waitTime(1000);
        // atest.popupContainsText("net growth = population*growth rate",dtest,client) //TODO: doesn't work for this popup
        assert(!dtest.isDonePopupVisible(client),"The Done hint popup is visible, but it should not be!");
    }));
    
    after(async(function(done){
        dtest.endTest(client);
    }));
});