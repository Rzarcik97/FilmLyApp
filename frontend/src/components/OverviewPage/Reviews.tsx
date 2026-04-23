import review_1 from '../../../public/reviews/review_1.png';
import review_2 from '../../../public/reviews/review_2.png';
import review_3 from '../../../public/reviews/review_3.png';

export const Reviews = () => {
  return (
    <section className="pt-18 flex justify-center items-center gap-6">
      <img src={review_1} alt="" className="hidden md:block"/>
      <img src={review_2} alt="" className="hidden md:block" />
      <img src={review_3} alt="" />
    </section>
  )
}