module.exports = function () {
	var fs = require('fs');
	var request = require('request');
	var Handlebars = require('handlebars');

	var Mail = function (options) {
		this.from = options.from || {
			name: "myapp.com",
			email: "myapp@pentcloud.com"
		};
		this.to = options.to || ["anthony.cifuentes@pentcloud.com"];
		this.subject = options.subject || "My cool app";
		this.body = options.body || "Empty email body";
	};

	Mail.prototype.send = function () {

		var $this = this;

		return new Promise(function (resolve, reject) {
			request.post('http://mail-server.pentcloud.com/mail', {
				form: {
					from: $this.from,
					to: $this.to,
					subject: $this.subject,
					html: $this.body
				}
			}, function (err, httpResponse, body) {
				if (err) {
					reject(err);
				} else {
					resolve(httpResponse, body);
				};
			});
		});
	};

	var mailer = {
		compose: function (options, data, template) {
			return new Promise(function (resolve, reject) {
				fs.readFile("./mailing/templates/" + template + ".html", "utf8", function (err, source) {
					if (err) {
						reject(err);
					} else {
						var template = Handlebars.compile(source);
						options.body = template(data);
						resolve(new Mail(options));
					};
				});
			});
		}
	};

	return mailer;
};