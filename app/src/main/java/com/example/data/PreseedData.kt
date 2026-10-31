package com.example.data

object PreseedData {
    val initialVideos = listOf(
        VideoItem(
            id = "vid_1",
            title = "Grand Akwa Ibom Cultural Ekombi Dance in Uyo",
            creator = "Ibom Cultural Troupe",
            handle = "@ibom_culture",
            category = "AkwaIbom",
            videoUrl = "https://filesamples.com/samples/video/mp4/sample_960x400_ocean_with_audio.mp4",
            thumbnailDescription = "Vibrant traditional attire, rhythmic drums and joyful smiles celebrating Akwa Ibom heritage",
            likesCount = 2840,
            commentsCount = 312,
            sharesCount = 680,
            soundTitle = "Ekombi Traditional Beat - Uyo Stars",
            durationText = "0:58"
        ),
        VideoItem(
            id = "vid_2",
            title = "Nollywood Short: The Secret of the Palm Wine Tapper",
            creator = "Calabar & Uyo Cinema",
            handle = "@nollywood_flair",
            category = "African",
            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
            thumbnailDescription = "Dramatic village scene with traditional wisdom, storytelling and suspense",
            likesCount = 5410,
            commentsCount = 890,
            sharesCount = 1420,
            soundTitle = "Highlife Groove & Folk Strings",
            durationText = "1:15"
        ),
        VideoItem(
            id = "vid_3",
            title = "Acrobatic Wushu Kung Fu Masters - Sunset Performance",
            creator = "Beijing Martial Arts",
            handle = "@master_wushu",
            category = "Chinese",
            videoUrl = "https://filesamples.com/samples/video/mp4/sample_640x360.mp4",
            thumbnailDescription = "Fast-paced martial arts swordplay, flips, and breathtaking synchronization",
            likesCount = 19200,
            commentsCount = 1430,
            sharesCount = 3890,
            soundTitle = "Traditional Guzheng & War Drum",
            durationText = "0:42"
        ),
        VideoItem(
            id = "vid_4",
            title = "Manila Acoustic Serenade - Harana Street Harmony",
            creator = "Pinoy Acoustic Vibes",
            handle = "@manila_acoustic",
            category = "Philippine",
            videoUrl = "https://archive.org/download/BigBuckBunny_328/BigBuckBunny_512kb.mp4",
            thumbnailDescription = "Heartfelt acoustic guitar duet under city fairy lights singing a classic ballad",
            likesCount = 8740,
            commentsCount = 620,
            sharesCount = 990,
            soundTitle = "Kundiman Soul - Acoustic Strings",
            durationText = "1:02"
        ),
        VideoItem(
            id = "vid_5",
            title = "Ibeno Beach Sunset Carnival & Boat Regatta",
            creator = "Explore Akwa Ibom",
            handle = "@ibeno_vibes",
            category = "AkwaIbom",
            videoUrl = "https://filesamples.com/samples/video/mp4/sample_1280x720_surfing_with_audio.mp4",
            thumbnailDescription = "Golden sand, Atlantic ocean waves, colorful canopies and beach soccer",
            likesCount = 4230,
            commentsCount = 415,
            sharesCount = 760,
            soundTitle = "Afrobeat Beach Mix - DJ Akwa",
            durationText = "0:50"
        ),
        VideoItem(
            id = "vid_6",
            title = "Chinese Sichuan Face-Changing (Bian Lian) Magic",
            creator = "Chengdu Cultural Arts",
            handle = "@bianlian_magic",
            category = "Chinese",
            videoUrl = "https://www.w3schools.com/html/movie.mp4",
            thumbnailDescription = "Master performer instantaneously transforming silk masks in front of a live audience",
            likesCount = 15300,
            commentsCount = 980,
            sharesCount = 2780,
            soundTitle = "Sichuan Opera Dramatic Percussion",
            durationText = "0:38"
        ),
        VideoItem(
            id = "vid_7",
            title = "Pinoy Street Food Feast in Cebu: Lechon & Halo-Halo",
            creator = "Cebu Island Foodie",
            handle = "@pinoy_eats",
            category = "Philippine",
            videoUrl = "https://filesamples.com/samples/video/mp4/sample_1280x720.mp4",
            thumbnailDescription = "Sizzling street delicacies, crispy lechon, sweet shaved ice halo-halo with ube",
            likesCount = 11200,
            commentsCount = 750,
            sharesCount = 1560,
            soundTitle = "Tropical Ukulele Sunshine",
            durationText = "0:55"
        ),
        VideoItem(
            id = "vid_8",
            title = "African Sci-Fi & Nollywood Short Movie: New Horizons",
            creator = "Lagos & Uyo Film Hub",
            handle = "@nollywood_future",
            category = "African",
            videoUrl = "https://archive.org/download/ElephantsDream/ed_hd.mp4",
            thumbnailDescription = "Futuristic storytelling blending indigenous African folklore and high fantasy",
            likesCount = 7890,
            commentsCount = 640,
            sharesCount = 1120,
            soundTitle = "Afro-Futurism Orchestra - Sound of Light",
            durationText = "1:30"
        )
    )

    val initialPosts = listOf(
        CommunityPost(
            id = "post_1",
            authorName = "Anietie Udoh",
            authorHandle = "@anietie_uyo",
            authorLga = "Uyo",
            content = "Excited to witness the vibrant tech and cultural ecosystem rising in Uyo! The youth are innovating in software, agriculture, and music. Brighter We Shine! #AkwaIbomRising #TheLight",
            category = "Tech",
            timestamp = "2 hours ago",
            likesCount = 142,
            commentsCount = 28
        ),
        CommunityPost(
            id = "post_2",
            authorName = "Ekaette Bassey",
            authorHandle = "@ekaette_b",
            authorLga = "Ikot Ekpene",
            content = "Visiting the Raffia City of Ikot Ekpene today. The craftsmanship on these bags, hats, and traditional chairs is unmatched. Support our local artisans!",
            category = "Culture",
            timestamp = "4 hours ago",
            likesCount = 205,
            commentsCount = 37
        ),
        CommunityPost(
            id = "post_3",
            authorName = "Dr. Effiong Asuquo",
            authorHandle = "@dr_effiong",
            authorLga = "Eket",
            content = "Medical outreach in Eket rural communities concluded today. Over 400 families received free screenings. Gratitude to all volunteers!",
            category = "News",
            timestamp = "Yesterday",
            likesCount = 380,
            commentsCount = 54
        ),
        CommunityPost(
            id = "post_4",
            authorName = "Idara Emmanuel",
            authorHandle = "@idara_vibes",
            authorLga = "Oron",
            content = "Nothing beats fresh seafood straight from Oron beach market! Afang soup and Edikang Ikong preparation starting shortly. Who is coming over?",
            category = "Lifestyle",
            timestamp = "Yesterday",
            likesCount = 490,
            commentsCount = 92
        )
    )

    val initialConversations = listOf(
        Conversation(
            id = "conv_group_1",
            name = "Akwa Ibom Global Connect",
            subtitle = "Active community of leaders & innovators",
            lastMessage = "Anietie: Welcome all new members to THE LIGHT platform!",
            timestamp = "10:30 AM",
            unreadCount = 2,
            isGroup = true,
            isIbibioZone = false
        ),
        Conversation(
            id = "conv_group_2",
            name = "Uyo Tech & Creative Hub",
            subtitle = "Coding, Design, Cinema & AI creators",
            lastMessage = "Emem: The hackathon registration closes this Friday.",
            timestamp = "09:15 AM",
            unreadCount = 0,
            isGroup = true,
            isIbibioZone = false
        ),
        Conversation(
            id = "conv_dm_1",
            name = "Mfoniso Akpan",
            subtitle = "Graphic Designer, Uyo",
            lastMessage = "I have uploaded the new posters for the Youth Festival.",
            timestamp = "Yesterday",
            unreadCount = 1,
            isGroup = false,
            isIbibioZone = false
        ),
        Conversation(
            id = "conv_ibibio_1",
            name = "Nneme Ibibio (Ibibio Lounge)",
            subtitle = "Kèm usem Ibibio kpọt mi!",
            lastMessage = "Ubong: Amesire nditọ Akwa Ibom! Idem mbufo?",
            timestamp = "11:00 AM",
            unreadCount = 3,
            isGroup = true,
            isIbibioZone = true
        )
    )

    val initialMessages = listOf(
        ChatMessage(
            id = "msg_1",
            conversationId = "conv_group_1",
            senderName = "Ekaette",
            content = "Greetings everyone! The Light is here to unite our people worldwide.",
            timestamp = "10:15 AM",
            isFromMe = false
        ),
        ChatMessage(
            id = "msg_2",
            conversationId = "conv_group_1",
            senderName = "Anietie",
            content = "Welcome all new members to THE LIGHT platform!",
            timestamp = "10:30 AM",
            isFromMe = false
        ),
        ChatMessage(
            id = "msg_3",
            conversationId = "conv_dm_1",
            senderName = "Mfoniso Akpan",
            content = "Hello brother, did you check the flyer designs for the exhibition?",
            timestamp = "Yesterday",
            isFromMe = false
        ),
        ChatMessage(
            id = "msg_4",
            conversationId = "conv_dm_1",
            senderName = "You",
            content = "Yes! The green and gold contrast looks majestic. Brighter We Shine!",
            timestamp = "Yesterday",
            isFromMe = true
        ),
        ChatMessage(
            id = "msg_5",
            conversationId = "conv_ibibio_1",
            senderName = "Ubong",
            content = "Amesire nditọ Akwa Ibom! Idem mbufo?",
            timestamp = "11:00 AM",
            isFromMe = false,
            isIbibioZone = true
        )
    )

    val initialDatingProfiles = listOf(
        DatingProfile(
            id = "date_1",
            name = "Iniobong",
            age = 25,
            location = "Uyo, Akwa Ibom",
            profession = "Architect & Interior Designer",
            bio = "Passionate about sustainable architecture, Afrobeats, traditional Afang soup cooking, and weekend tennis. Looking for someone genuine and ambitious. 18+",
            interests = "Design, Travel, Music, Tennis, Culture"
        ),
        DatingProfile(
            id = "date_2",
            name = "Kufre",
            age = 28,
            location = "Eket, Akwa Ibom",
            profession = "Petroleum Engineer & Tech Enthusiast",
            bio = "Fitness lover, avid reader, and entrepreneur. Let us explore the quiet beaches of Ibeno and discuss visionary ideas. 18+",
            interests = "Engineering, Fitness, Beach Walks, Books"
        ),
        DatingProfile(
            id = "date_3",
            name = "Blessing",
            age = 24,
            location = "Ikot Ekpene / Lagos",
            profession = "Fashion Designer & Creative Director",
            bio = "Blending modern fashion with indigenous raffia art. I love good conversations, spontaneous road trips, and jazz. 18+",
            interests = "Fashion, Photography, Art, Jazz, Road Trips"
        ),
        DatingProfile(
            id = "date_4",
            name = "Edidiong",
            age = 29,
            location = "Uyo, Akwa Ibom",
            profession = "Software Developer & Pianist",
            bio = "Building open-source tools by day, playing classical & contemporary melodies by night. Looking for a partner in purpose and laughter. 18+",
            interests = "Coding, Piano, Coffee, Innovation, Cinema"
        )
    )

    val initialIbibioPhrases = listOf(
        IbibioPhrase(
            id = "phr_1",
            ibibioText = "Amesiere",
            englishTranslation = "Good morning (Are you awake?)",
            pronunciationGuide = "Ah-meh-see-eh-reh",
            category = "Greetings"
        ),
        IbibioPhrase(
            id = "phr_2",
            ibibioText = "Idem mfo?",
            englishTranslation = "How are you? (How is your body?)",
            pronunciationGuide = "Ee-dem m-foh?",
            category = "Greetings"
        ),
        IbibioPhrase(
            id = "phr_3",
            ibibioText = "Idem mi ọsọn",
            englishTranslation = "I am fine / My body is strong",
            pronunciationGuide = "Ee-dem mee oh-sohn",
            category = "Greetings"
        ),
        IbibioPhrase(
            id = "phr_4",
            ibibioText = "Sosongo",
            englishTranslation = "Thank you",
            pronunciationGuide = "Soh-sohn-goh",
            category = "Daily"
        ),
        IbibioPhrase(
            id = "phr_5",
            ibibioText = "Abasi ọdiọñ fi",
            englishTranslation = "God bless you",
            pronunciationGuide = "Ah-bah-see oh-dee-onh fee",
            category = "Respect"
        ),
        IbibioPhrase(
            id = "phr_6",
            ibibioText = "Kpekpe unen idighe kpekpe ebot",
            englishTranslation = "A proverb: The custom of the fowl is not the custom of the goat (Respect uniqueness)",
            pronunciationGuide = "Kpeh-kpeh oo-nen ee-dee-gheh...",
            category = "Proverbs"
        ),
        IbibioPhrase(
            id = "phr_7",
            ibibioText = "Emem ye Ima",
            englishTranslation = "Peace and Love",
            pronunciationGuide = "Eh-mem yeh Ee-mah",
            category = "Culture"
        ),
        IbibioPhrase(
            id = "phr_8",
            ibibioText = "Mbọk",
            englishTranslation = "Please / I beg you",
            pronunciationGuide = "M-bohk",
            category = "Daily"
        )
    )

    val initialFixtures = listOf(
        SportsFixture(
            id = "fix_1",
            league = "EPL",
            homeTeam = "Arsenal",
            awayTeam = "Manchester City",
            homeScore = 2,
            awayScore = 1,
            status = "LIVE",
            minute = "74'",
            dateStr = "Today",
            venue = "Emirates Stadium, London"
        ),
        SportsFixture(
            id = "fix_2",
            league = "EPL",
            homeTeam = "Liverpool",
            awayTeam = "Chelsea",
            homeScore = 3,
            awayScore = 0,
            status = "FT",
            minute = "Full Time",
            dateStr = "Today",
            venue = "Anfield, Liverpool"
        ),
        SportsFixture(
            id = "fix_3",
            league = "EPL",
            homeTeam = "Manchester United",
            awayTeam = "Newcastle United",
            homeScore = 0,
            awayScore = 0,
            status = "UPCOMING",
            minute = "20:00",
            dateStr = "Tomorrow",
            venue = "Old Trafford, Manchester"
        ),
        SportsFixture(
            id = "fix_4",
            league = "UCL",
            homeTeam = "Real Madrid",
            awayTeam = "Bayern Munich",
            homeScore = 2,
            awayScore = 2,
            status = "FT",
            minute = "Full Time",
            dateStr = "Quarter-Final",
            venue = "Santiago Bernabéu, Madrid"
        ),
        SportsFixture(
            id = "fix_5",
            league = "UCL",
            homeTeam = "Barcelona",
            awayTeam = "Paris Saint-Germain",
            homeScore = 1,
            awayScore = 0,
            status = "UPCOMING",
            minute = "20:00",
            dateStr = "Wednesday",
            venue = "Camp Nou, Barcelona"
        )
    )

    val initialEntertainment = listOf(
        EntertainmentItem(
            id = "ent_1",
            title = "Akwa Ibom Film Festival Announces 2026 Nominees",
            category = "Nollywood",
            author = "Cinema Desk",
            snippet = "Over 60 original movies shot in Uyo, Eket and Ikot Ekpene receive international film festival acclaim.",
            dateStr = "Today",
            readTime = "3 min read",
            sourceName = "Ibom Cinema Herald"
        ),
        EntertainmentItem(
            id = "ent_2",
            title = "Afrobeats Global Tour: Stars Set to Perform at Godswill Akpabio Stadium",
            category = "Music",
            author = "Music Wire",
            snippet = "Major concert lineup announced for December homecoming concert in the Nest of Champions.",
            dateStr = "Yesterday",
            readTime = "4 min read",
            sourceName = "Akwa Music Hub"
        ),
        EntertainmentItem(
            id = "ent_3",
            title = "Ini Edo Highlights Women in Creative Arts & Akwa Ibom Heritage",
            category = "Celebrity",
            author = "Lifestyle Bureau",
            snippet = "Veteran actress inspires thousands of young creatives at the Uyo Youth Empowerment Summit.",
            dateStr = "2 days ago",
            readTime = "2 min read",
            sourceName = "Naija Star Pulse"
        )
    )

    val initialOpportunities = listOf(
        OpportunityItem(
            id = "opp_1",
            title = "Senior Mobile Android Engineer",
            organization = "Ibom Innovation Network (IIN)",
            category = "Jobs",
            location = "Uyo, Akwa Ibom (Hybrid)",
            deadline = "15 Nov 2026",
            description = "Build cutting-edge digital platforms for government, logistics, and education across West Africa.",
            requirements = "Kotlin, Jetpack Compose, Room, MVVM, 3+ years Android experience.",
            contactOrUrl = "careers@ibominnovation.org"
        ),
        OpportunityItem(
            id = "opp_2",
            title = "State Undergraduate Merit Scholarship",
            organization = "Akwa Ibom State Scholarship Board",
            category = "Scholarships",
            location = "Akwa Ibom & Federal Universities",
            deadline = "30 Nov 2026",
            description = "Full tuition and living stipend for exceptional students enrolled in STEM, Medicine, and Law programs.",
            requirements = "Minimum 3.5 CGPA, Certificate of Origin from Akwa Ibom LGA.",
            contactOrUrl = "scholarships@akwaibomstate.gov.ng"
        ),
        OpportunityItem(
            id = "opp_3",
            title = "Agro-Enterprise SME Seed Grant (₦5,000,000)",
            organization = "Delta Coast Agricultural Fund",
            category = "Business Grants",
            location = "All 31 LGAs of Akwa Ibom",
            deadline = "10 Dec 2026",
            description = "Non-repayable grants for youth and women farmers expanding cassava, oil palm, fishery, and poultry production.",
            requirements = "Registered business name (CAC), farm location proof, 2-page business summary.",
            contactOrUrl = "grants@deltacoastfund.org"
        ),
        OpportunityItem(
            id = "opp_4",
            title = "Digital Skills & Software Bootcamp",
            organization = "Akwa Ibom Tech Academy",
            category = "Skills Training",
            location = "E-Library Complex, Uyo",
            deadline = "20 Nov 2026",
            description = "Free 12-week intensive practical training in Full-Stack Web Development, Data Analysis, and UI/UX Design.",
            requirements = "Basic computer literacy and commitment to complete coursework.",
            contactOrUrl = "academy@akwaibomtech.org"
        ),
        OpportunityItem(
            id = "opp_5",
            title = "Discover Ibeno Beach & Mangrove Eco-Tour",
            organization = "Akwa Ibom Tourism Board",
            category = "Tourism & Heritage",
            location = "Ibeno LGA, Akwa Ibom",
            deadline = "Year-Round",
            description = "Experience the longest sand beach in West Africa, boat cruises, fresh seafood grills, and mangrove boat tours.",
            requirements = "Open to tourists, families, and school excursions.",
            contactOrUrl = "tourism@akwaibomstate.gov.ng"
        )
    )

    val initialAnnouncements = listOf(
        AnnouncementItem(
            id = "ann_1",
            title = "Welcome to THE LIGHT of Akwa Ibom!",
            content = "Connecting our people at home and in the diaspora. 'BRIGHTER WE SHINE!' Explore videos, community discussions, dating, sports, and language.",
            badge = "OFFICIAL",
            dateStr = "Today"
        ),
        AnnouncementItem(
            id = "ann_2",
            title = "Ibibio Language Zone Code of Conduct",
            content = "Please observe our cultural guidelines: speak and write strictly in Ibibio in the designated lounge. Let us preserve our beautiful heritage.",
            badge = "CULTURE",
            dateStr = "Yesterday"
        )
    )
}
