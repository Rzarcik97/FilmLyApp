import { useState } from 'react';
import pic_1 from '../../../public/reviews/pic_1.png';
import pic_2 from '../../../public/reviews/pic_2.png';
import pic_3 from '../../../public/reviews/pic_3.png';
import pic_4 from '../../../public/reviews/pic_4.png';
import rating from '../../../public/reviews/rating.svg';
import { Toast } from '../Profile/Toast';

const MOCK_REVIEWS = [
  {
    id: 1,
    profilePic: pic_1,
    name: 'Aelin Rodriguez',
    text: "This is an engagingly simple, good-hearted film, with just enough darkness around the edges to give contrast and relief to its glowingly benign view of human nature.",
    date: 'Thuesday'
  },
  {
    id: 2,
    profilePic: pic_2,
    name: 'Sarah Ahmad',
    text: "A tough, complex story [told] with clarity, compassion and considerable dramatic force.",
    date: 'March 22, 2024'
  },
  {
    id: 3,
    profilePic: pic_3,
    name: 'Abdullah Sharief',
    text: "It doesn't matter how we think we remember the moments, how they look now, in this edition, will force a shift in memory that we'll most likely be grateful for.",
    date: 'September 16, 2023'
  },
  {
    id: 4,
    profilePic: pic_4,
    name: 'Marshall Jenkins',
    text: "Finally, a film that treats the audience with intelligence! No over-explaining the plot, just pure visual storytelling. Filmly really nailed it with this recommendation.",
    date: 'August 10, 2023'
  }
]

export const ReviewCards = () => {
  const [showToast, setShowToast] = useState(false);

  const handleToast = () => {
    setShowToast(true);
    setTimeout(() => setShowToast(false), 2000);
  };

  return (
    <section className="py-10 px-4 md:px-12">
      <div className="flex justify-between items-center pb-3 md:pb-6">
        <div className="flex gap-2 items-center">
          <div className="w-1 h-[35px] bg-primary-0 rounded-full" />
          <h2 className="text-[24px] md:text-[36px] leading-[1.2] font-bold text-secondary-light">Audience  Reviews</h2>
        </div>

        <button
          onClick={handleToast}
          className="cursor-pointer text-[16px] text-secondary-light font-nunito font-bold"
        >
          View all
        </button>
        <Toast isVisible={showToast} />
      </div>

      <div className="flex justify-center md:justify-between items-start">
        {MOCK_REVIEWS.map(review => (
          <div
            key={review.id}
            className={`p-3 bg-gray-90 flex flex-col gap-[11px] rounded-[8px] max-w-80
            ${review.id === 1 ? 'flex' : 'hidden'}
            ${review.id === 2 ? 'md:flex' : ''}
            ${review.id > 2 ? 'lg:flex' : ''}
            `}
          >
            <div className="flex gap-[5px] items-center">
              <img src={review.profilePic} alt="Profile pic" className="w-10 h-10 rounded-full" />
              <div className="flex flex-col items-start">
                <h2 className="font-nunito text-secondary-light text-[24px] leading-[1.3] font-bold">{review.name}</h2>
                <img src={rating} alt="Rating" className="w-[53px]" />
              </div>
            </div>
            <p className="font-nunito text-secondary-light text-[16px] leading-[1.35]">{review.text}</p>
            <p className="text-gray-30 text-[12px] text-right">{review.date}</p>
          </div>
        ))}
      </div>
    </section>
  )
}