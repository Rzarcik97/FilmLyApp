import { Login } from './Login'

export const LoginPage = () => {
  const baseColor = 'rgba(17, 17, 16, 1)';

  return (
    <main
      className="relative py-24 bg-cover bg-center bg-no-repeat"
      style={{ backgroundImage: "url('/backgrounds/sign_up.png')" }}
    >
      <Login />

      <div
        className="absolute inset-x-0 bottom-0 h-40 z-0"
        style={{
          background: `linear-gradient(to bottom, transparent, ${baseColor})`
        }}
      />
    </main>
  )
}