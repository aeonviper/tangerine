var cookieName = 'tanyatutor-language';

function getCookie(name) {
	name = name + "=";
	var cookies = document.cookie.split(";");
	for (var i=0;i<cookies.length;i++) {
		var cookie = cookies[i];
		if (cookie.indexOf(name) != -1) {
			while (cookie.charAt(0) == ' ') {
				cookie = cookie.substring(1);
			}
			if (cookie.indexOf(name) == 0) {
				return cookie.substring(name.length, cookie.length);
			}
		}		
	}
	return "";
};

function determineLanguage() {
	language = languages.id;
	var cookie = getCookie(cookieName);
	if (cookie != '') {
		if (cookie.indexOf('english') != -1) {
			language = languages.en;
		}
	}
};

function setLanguage(name) {
	document.cookie = cookieName + "=" + name + "; expires=Fri, 1 Jan 2027 12:00:00 UTC; path=/";
	window.location.reload();
};

var language;

var languages = new Object();
languages.en = new Object();
languages.id = new Object();

languages.en.identity = 'en';
languages.id.identity = 'id';

languages.en.email = "Email";
languages.id.email = "Email";

languages.en.login = "Login";
languages.id.login = "Masuk";

languages.en.signIn = "Sign in";
languages.id.signIn = "Masuk";

languages.en.password = "Password";
languages.id.password = "Password";

languages.en.register = "Register";
languages.id.register = "Daftar";

languages.en.next = "Next";
languages.id.next = "Lanjut";

languages.en.newPassword = "New Password";
languages.id.newPassword = "Password Baru";

languages.en.confirmNewPassword = "Confirm New Password";
languages.id.confirmNewPassword = "Ulangi Password Baru";

languages.en.resetPassword = "Reset Password";
languages.id.resetPassword = "Reset Password";

languages.en.verificationCode = "Verification Code";
languages.id.verificationCode = "Kode Verifikasi";

languages.en.verify = "Verify";
languages.id.verify = "Verifikasi";

languages.en.name = "Name";
languages.id.name = "Nama";

languages.en.phone = "Phone";
languages.id.phone = "Telepon";

languages.en.school = "School";
languages.id.school = "Sekolah";

languages.en.confirmPassword = "Confirm Password";
languages.id.confirmPassword = "Ulangi Password";

languages.en.registrationCode = "Registration Code";
languages.id.registrationCode = "Kode Daftar";

languages.en.areYouTutor = "Register as a tutor ?";
languages.id.areYouTutor = "Daftar sebagai pengajar ?";

languages.en.yes = "Yes";
languages.id.yes = "Ya";

languages.en.no = "No";
languages.id.no = "Tidak";

languages.en.conversation = 'Conversation';
languages.id.conversation = 'Pembicaraan';

languages.en.betweenYou = 'between You and';
languages.id.betweenYou = 'antara Kamu dan';

languages.en.edit = "Edit";
languages.id.edit = "Ubah";

languages.en.change = "Change";
languages.id.change = "Ganti";

languages.en.delete = "Delete";
languages.id.delete = "Hapus";

languages.en.remove = "Remove";
languages.id.remove = "Hapus";

languages.en.yourQuestion = "Your question";
languages.id.yourQuestion = "Tulis pertanyaanmu";

languages.en.yourAnswer = "Your answer";
languages.id.yourAnswer = "Jawabanmu";

languages.en.photoDescription = "Photo description";
languages.id.photoDescription = "Deskripsi foto";

languages.en.send = "Send";
languages.id.send = "Kirim";

languages.en.buy = "Buy";
languages.id.buy = "Beli";

languages.en.standardSubscription = "Standard Subscription";
languages.id.standardSubscription = "Paket Standard";

languages.en.goldSubscription = "Gold Subscription";
languages.id.goldSubscription = "Paket Gold";

languages.en.platinumSubscription = "Platinum Subscription";
languages.id.platinumSubscription = "Paket Platinum";

languages.en.notEnoughCreditToBuyPackage = "You don't have enough credit balance to buy this package";
languages.id.notEnoughCreditToBuyPackage = "Saldo kredit anda tidak cukup untuk membeli paket ini";

languages.en.profile = "Profile";
languages.id.profile = "Profil";

languages.en.cancel = "Cancel";
languages.id.cancel = "Batal";

languages.en.verifyNewEmail = "Verify New Email";
languages.id.verifyNewEmail = "Ganti Email";

languages.en.currentPassword = "Current Password";
languages.id.currentPassword = "Password Sekarang";

languages.en.by = "by";
languages.id.by = "oleh";

languages.en.ask = "Ask";
languages.id.ask = "Tanya";

languages.en.chat = "Chats";
languages.id.chat = "Diskusi";

languages.en.setting = "Settings";
languages.id.setting = "Settings";

languages.en.logout = "Logout";
languages.id.logout = "Keluar";

languages.en.previousPage = "Previous";
languages.id.previousPage = "Sebelum";

languages.en.nextPage = "Next";
languages.id.nextPage = "Berikut";

languages.en.photo = "Photo";
languages.id.photo = "Foto";

languages.en.question = "Question";
languages.id.question = "Pertanyaan";

languages.en.explanation = "Explanation";
languages.id.explanation = "Penjelasan";

languages.en.subject = "Subject";
languages.id.subject = "Pelajaran";

languages.en.level = "Class";
languages.id.level = "Kelas";

languages.en.review = "Review";
languages.id.review = "Review";

languages.en.postedOn = "Posted on";
languages.id.postedOn = "Dibuat pada";

languages.en.close = "Close";
languages.id.close = "Tutup";

languages.en.reopen = "Reopen";
languages.id.reopen = "Buka";

determineLanguage();














// ---------------------------

languages.en.textVerificationCodeSent = "A verification code has been sent to your email address, please enter it here";
languages.id.textVerificationCodeSent = "Kode verifikasi telah dikirim ke email address anda, silahkan masukkan disini";

languages.en.textForgottenYourPassword = "Forgotten your password ?";
languages.id.textForgottenYourPassword = "Lupa password anda ?";
