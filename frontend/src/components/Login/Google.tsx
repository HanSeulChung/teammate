const Google = () => {
  const googleClientId =
    "988799632073-ghj80ioi4mo9jnb9dchjbjtaucmrmm81.apps.googleusercontent.com";
  const googleRedirectUrl = "http://localhost:3000/googleLogin";
  const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${googleClientId}&scope=openid%20profile%20email&redirect_uri=${googleRedirectUrl}`;

  const loginHandler = () => {
    window.location.href = googleAuthUrl;
  };
  return (
    <div>
      <button
        className="btn btn-wide btn-ghost border-slate-300 no-animation"
        onClick={loginHandler}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
            d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
          />
        </svg>
        구글 계정으로 로그인
      </button>
    </div>
  );
};

export default Google;
