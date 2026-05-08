import { Star } from 'lucide-react'
import { useState } from 'react';
import resize from '../../../public/icons/resize.svg';

export const UserReview = ({ title }: { title: string | null }) => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");

  const isInteracting = rating > 0 || comment.trim().length > 0;

  return (
    <section className="py-10 px-4 md:px-12">
      <div className="flex justify-between items-center pb-3 md:pb-6">
        <div className="flex gap-2 items-center">
          <div className="w-1 h-[35px] bg-primary-0 rounded-full" />
          <h2 className="text-[24px] md:text-[36px] leading-[1.2] font-bold text-secondary-light">Your review of the movie: {title}</h2>
        </div>

        <button
          className="cursor-pointer text-[16px] text-secondary-light font-nunito font-bold">
          View all
        </button>
      </div>

      <div className="bg-gray-90 rounded-[16px] p-4">
        <div className="flex gap-2 justify-between md:justify-start pb-3 md:pb-6">
          {[1, 2, 3, 4, 5].map((starNumber) => (
            <Star
              key={starNumber}
              className={`w-12 h-12 md:w-14 md:h-14 cursor-pointer transition-colors duration-200 text-primary-0 ${starNumber <= rating ? 'text-primary-0 fill-primary-0' : 'text-primary-0'
                }`}
              onClick={() => setRating(starNumber)}
            />
          ))}
        </div>
        <p className="text-gray-30 text-[16px] md:text-[20px] leading-1.45 font-semibold pb-2 md:pd-4">What did you think of this movie?</p>
        <div className="relative w-full mb-3 md:mb-6">
          <input
            type="text"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="Leave your review here..."
            className="text-gray-30 p-2 pb-6 h-16 bg-gray-100 rounded-[8px] w-full outline-none border-1 border-transparent focus:border-primary-0 p-2"
          />
          <img
            src={resize}
            alt=""
            className="absolute bottom-2 right-2 z-10 w-4 md:w-6 h-4 md:h-6 pointer-events-none"
          />
        </div>

        {isInteracting && (
          <div className="flex justify-start animate-fade-in">
            <button className="bg-primary-0 text-secondary-dark font-bold px-8 py-2 rounded-[9px] cursor-pointer hover:opacity-90 transition-all w-full md:w-78 h-10 md:h-14">
              Add
            </button>
          </div>
        )}
      </div>
    </section>
  )
}