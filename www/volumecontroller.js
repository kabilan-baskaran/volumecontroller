var exec = require('cordova/exec');

var volumecontroller = {
    beep: function (arg0) {
        let defaultedCount = (arg0 && Number.isInteger(arg0)) ? arg0 : 1;
        exec(null, null, 'volumecontroller', 'beep', [defaultedCount]);
    },
    setVolume: function (arg0, success, error) {
        let defaultedVolume = (arg0 && Number.isInteger(arg0)) ? arg0 : 0.5;
        exec(success, error, 'volumecontroller', 'setVolume', [defaultedVolume]);
    },
    getMaxVolume: function (success, error) {
        exec(success, error, 'volumecontroller', 'getMaxVolume', []);
    },
    getVolume: function (success, error) {
        exec(success, error, 'volumecontroller', 'getVolume', []);
    }

};
module.exports = volumecontroller;
