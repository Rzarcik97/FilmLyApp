import { useSelector } from 'react-redux'
import type { RootState } from '../../store'

export const ProfileMain = ({ name }: { name: string }) => {
  return (
    <main
      className="bg-gray-100 px-6 md:px-12 min-h-screen md:h-[672px] md:pb-8
      bg-cover bg-center bg-no-repeat
      flex flex-col relative"
    >
      <div
        className="absolute inset-0 z-0 bg-cover bg-center bg-no-repeat animate-fade-in"
        style={{
          backgroundImage: `url('/backgrounds/profile.png')`
        }}
      />

      <div
        className="absolute inset-0 z-10 bg-gradient-to-t from-gray-100 via-gray-100/40 to-transparent"
      />

      <div className="flex-1 flex flex-col justify-start items-end md:flex-row h-full pb-6 relative z-10">
        <div className="mt-auto lg:m-0 w-full max-w-[540px] lg:max-w-[432px] lg:mr-[85px] md:w-[432px] md:mr-[85px] shrink-0 order-2 md:order-1 flex flex-col justify-end">
          <h1 className="lg:m-0 text-[28px] md:text-[48px] text-primary-0 font-bold">
            Hello, {name}!
          </h1>
          <h1 className="lg:m-0 text-[32px] lg:text-[64px] leading-none text-secondary-light font-bold">
            Watchlist
          </h1>
          <p className="m-0 py-2 text-[16px] md:text-[20px] leading-[1.45] tracking-[-0.011em] text-gray-0 font-medium font-nunito">
            It’s you personal cinema space.
            It’s where you collect everything that caught your eye: a trailer, a poster, a friend’s recommendation, or a film you found at 3 a.m. It’s not “I’ll watch it later” -
            it’s future emotions. 
          </p>
        </div>


      </div>
    </main >
  )
}