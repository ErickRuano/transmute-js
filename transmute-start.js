#!/usr/bin/env node

// Dependencies
var co          = require('co');
var prompt      = require('co-prompt');
var program     = require('commander');
var request     = require('request');

// Modules
var nigredo, albedo, citrinitas, rubedo;
 
// Start command
program
  .option('-l, --lead <lead>', 'Select the subject material [lead]', 'lead')
  .option('-c, --circle <circle>', 'Select the circle to use [circle]', 'spring')
  .parse(process.argv);

    // Intro animation definition
    var introduction = function(){
      return new Promise(function(resolve, reject){
        console.log(
          [
            '🜀  🜁 🜂 🜃 🜄 🜅 🜆 🜇 🜈 🜉 🜊 🜋 🜌 🜍 🜎 🜏 🜐 🜑 🜒 🜓 🜔 🜕 🜖 🜗 🜘 🜙 🜚 🜛 🜜\n',
            '🜝  🜞 🜟 🜠 🜡 🜢 🜣 🜤 🜥 🜦 🜧 🜨 🜩 🜪 🜫 🜬 🜭 🜮 🜯 🜰 🜱 🜲 🜳 🜴 🜵 🜶 🜷 🜸 🜹\n',
            '🜺  🜻 🜼 🜽 🜾 🜿 🝀 🝁 🝂 🝃 🝄 🝅 🝆 🝇 🝈 🝉 🝊 🝋 🝌 🝍 🝎 🝏 🝐 🝑 🝒 🝓 🝔 🝕 🝖\n',
            '🝗  🝘 🝙 🝚 🝛 🝜 🝝 🝞 🝟 🝠 🝡 🝢 🝣 🝤 🝥 🝦 🝧 🝨 🝩 🝪 🝫 🝬 🝭 🝮 🝯 🝰 🝱 🝲 🝳'
          ].join("")
        );

        // Modules
        nigredo     = require('./nigredo.js')({ lead : program.lead+'.json', circle : program.circle });
        albedo      = require('./albedo.js');
        citrinitas  = require('./citrinitas.js');
        rubedo      = require('./rubedo.js');

        resolve();
      });
      
    }
     
    // Execute intro animation and initiate transmute on callback
    introduction().then(function(){
      co(function *() {
        // var username = yield prompt('username: ');
        // var password = yield prompt.password('password: ');
        // request.post({url:'http://52.70.161.163:14439/login', form: {username:username, password:password}}, function(err,httpResponse,body){ console.log(body) })


        nigredo().then(function(massa_confusa){
          albedo(massa_confusa).then(function(silver){
            citrinitas(silver).then(function(gold){
              rubedo(gold)
              // .then(function(){
                // SUCCESS
              // });
            });
          });
        });

      });
    })