import { ChevronRight } from 'lucide-react';
import play from '../../../public/icons/play.png';
import mute from '../../../public/icons/mute.png';
import { useState } from 'react';

const heroSection = [
  {
    id: 1,
    title: "All Of Us Strangers",
    info: "Drama  •  2023  •  1h 45m",
    bg: "/backgrounds/All_Of_Us_Strangers.png"
  },
  {
    id: 2,
    title: "The Batman",
    info: "Action  •  2022  •  2h 56m",
    bg: "/backgrounds/the_batman.jpg"
  },
  {
    id: 3,
    title: "The Departed",
    info: "Thriller  •  2006  •  2h 31m",
    bg: "/backgrounds/departed.jpg"
  },
  {
    id: 4,
    title: "Interstellar",
    info: "Sci-Fi  •  2014  •  2h 49m",
    bg: "/backgrounds/interstellar.jpg"
  },
  {
    id: 5,
    title: "Dune: Part Two",
    info: "Action  •  2024  •  2h 46m",
    bg: "/backgrounds/dune.webp"
  }
];

export const AboutUs = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const currentMovie = heroSection[currentIndex];

  const handleNext = () => {
    setCurrentIndex((prev) => (prev + 1) % heroSection.length);
  };

  const heavyGradient = `linear-gradient(to bottom, rgba(0,0,0,0.3) 0%, rgba(17,17,16,0.9) 100%)`;
  const lightGradient = `linear-gradient(to bottom, rgba(0,0,0,0.1) 0%, rgba(17,17,16,0.4) 100%)`;
  return (
    <main 
      className="bg-gray-100 px-6 md:px-12 min-h-screen md:h-[672px] md:pb-8
      bg-cover bg-center bg-no-repeat
      flex flex-col relative"
    >
      <div
        key={currentIndex}
        className="absolute inset-0 z-0 bg-cover bg-center bg-no-repeat animate-fade-in"
        style={{
          backgroundImage: `${currentIndex === 0 ? lightGradient : heavyGradient}, url(${currentMovie?.bg})`
        }}
      />

      <div className="flex-1 flex flex-col justify-end items-end md:flex-row h-full pb-6 relative z-10">
        <div className="mt-auto lg:m-0 w-full max-w-[540px] lg:max-w-[432px] lg:mr-[85px] md:w-[432px] md:mr-[85px] shrink-0 order-2 md:order-1 flex flex-col justify-end">
          <h1 className="lg:m-0 text-5xl text-gray-0 font-bold">
            About us
          </h1>
          <p className="m-0 py-[18px] text-lg md:text-xl leading-[1.45] tracking-[-0.011em] text-gray-0 font-medium">
            This is a platform  for discovering and choosing movies and TV shows
            that helps users quickly find what to watch, get personalized
            recommendations, and instantly see where the content is available.
          </p>
          <button className="bg-gray-0 border-none w-full lg:w-full h-11 rounded-[32px] text-[16px] leading-[1.5] tracking-[-0.011em] font-bold font-nunito cursor-pointer">
            Get started
          </button>
        </div>

        <div className="flex-1 flex flex-col justify-end lg:justify-between items-end md:pt-[296px] pb-10 lg:pb-8 min-h-full order-1 md:order-2">
          <button
            className="text-primary-0 cursor-pointer 
            w-12 h-12 hidden lg:flex justify-center items-center
            bg-gray-80/10 backdrop-blur-[2px]
            rounded-full border border-gray-80/10
            before:content-[''] before:absolute before:inset-0
            before:rounded-full before:border before:border-white/20"
            onClick={handleNext}
          >
            <ChevronRight size={24} />
          </button>

          <div className="flex flex-col justify-center items-center">
            <p className="text-[16px] text-gray-50 md:text-gray-80 leading-[1.5]">{currentMovie?.title}</p>
            <p className="text-[16px] text-gray-50 md:text-gray-80 leading-[1.5] pb-2">{currentMovie?.info}</p>
            <div className="flex gap-[30px] justify-center items-center">
              <img src={play} alt="Play button" className="cursor-pointer w-8 h-8" />
              <img src={mute} alt="Mute button" className="cursor-pointer w-8 h-8" />
            </div>
          </div>
        </div>

        <div className="absolute bottom-6 left-1/2 -translate-x-1/2 flex items-center gap-[3px] z-20">
          {heroSection.map((_, index) => (
            <button
              key={index}
              onClick={() => setCurrentIndex(index)}
              className={`transition-all duration-300 ${currentIndex === index
                  ? 'w-[24px] h-[6px] rounded-[3px] bg-primary-0'
                  : 'w-[6px] h-[6px] rounded-full bg-gray-0 hover:bg-gray-30'
                }`}
            />
          ))}
        </div>
      </div>
    </main >
  )
}
