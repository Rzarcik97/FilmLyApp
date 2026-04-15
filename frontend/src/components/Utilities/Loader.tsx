import { motion } from 'framer-motion';

export const Loader = () => {
  const orbitElements = [
    { color: 'bg-[#353E07]', width: 106, speed: 3.5, blur: 'blur-none' },
    { color: 'bg-primary-20', width: 69, speed: 3.2, blur: 'blur-[1px]' },
    { color: 'bg-primary-10', width: 41, speed: 2.8, blur: 'blur-[1px]' },
    { color: 'bg-[#BBD727]', width: 28, speed: 2.5, blur: 'blur-[2px]' },
    { color: 'bg-primary-0', width: 15, speed: 2.2, blur: 'blur-[3px]' },
    { color: 'bg-secondary-light', width: 6, speed: 1.8, blur: 'blur-[4px]' },
  ];

  const centerCore = { color: 'bg-[#F7FFBE]', width: 2 };

  const system = [
    ...orbitElements,
    centerCore,
    ...[...orbitElements].reverse()
  ];

  return (
    <div className="fixed inset-0 w-screen h-screen bg-black flex flex-col items-center justify-center z-[9999] overflow-hidden">

      <div className="absolute w-64 h-64 bg-[#BBD727]/10 rounded-full blur-[100px]" />

      <div className="flex items-center justify-center relative [perspective:1200px] [transform-style:preserve-3d]">
        {system.map((item, i) => {
          const isCenter = i === 6;
          const distFromCenter = Math.abs(i - 6);
          const orbitSpeed = isCenter ? 0 : (item as any).speed || 3;

          return (
            <motion.div
              key={i}
              className={`absolute rounded-full ${item.color} ${(item as any).blur || ''}`}
              style={{
                height: '114px',
                width: `${item.width}px`,
                borderRadius: '50% / 50%',
                boxShadow: `0 0 15px ${i < 6 ? 'rgba(53, 62, 7, 0.3)' : 'rgba(187, 215, 39, 0.2)'}`,
                border: '1px solid rgba(255,255,255,0.05)',
              }}
              animate={!isCenter ? {
                rotateY: [0, 360],
                scale: [1, 1.1, 0.9, 1],
                opacity: [0.3, 0.9, 0.3],
              } : {
                scaleY: [1, 1.2, 1],
                opacity: [0.5, 1, 0.5],
              }}
              transition={{
                duration: orbitSpeed,
                repeat: Infinity,
                ease: "easeInOut",
                delay: i * 0.15,
              }}
            />
          );
        })}
      </div>

      <div className="mt-32">
        <motion.div
          className="flex items-center tracking-[0.4em] uppercase text-[12px] font-medium text-[#BBD727]/80"
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1 }}
        >

          <span className="ml-4 flex items-center">
            Loading
            <span className="flex ml-2 w-8">
              {[0, 1, 2].map((dot) => (
                <motion.span
                  key={dot}
                  className="w-1 h-1 bg-[#BBD727] rounded-full mx-[2px]"
                  animate={{
                    scale: [0.5, 1.2, 0.5],
                    opacity: [0.2, 1, 0.2]
                  }}
                  transition={{
                    duration: 1.5,
                    repeat: Infinity,
                    delay: dot * 0.2,
                    ease: "easeInOut"
                  }}
                />
              ))}
            </span>
          </span>
        </motion.div>
      </div>
    </div>
  );
};